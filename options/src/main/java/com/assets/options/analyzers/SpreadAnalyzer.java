package com.assets.options.analyzers;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Option;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.exceptions.OptionsException;
import com.assets.options.entities.spread.vertical.VerticalSpread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.finmath.functions.NormalDistribution.cumulativeDistribution;

public class SpreadAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(SpreadAnalyzer.class);

    public static final BigDecimal DISTANCE_FROM_AVERAGE_STRIKE = BigDecimal.valueOf(50);
    public static final BigDecimal STEPS = BigDecimal.valueOf(30000);
    public static final BigDecimal DAYS_OF_YEAR = BigDecimal.valueOf(365);

    public List<SpreadAnalyzerResult> analyzeByDate(OptionBook book) {
        return book.getAvailableDates()
                .stream()
                .map(date -> getBestVerticalSpread(date, book))
                .filter(t -> t.isPresent())
                .map(t -> t.get())
                .collect(Collectors.toList());
    }

    private Optional<SpreadAnalyzerResult> getBestVerticalSpread(LocalDate date, OptionBook optionBook) {
        List<Option> options = optionBook.getOptions(date);
        List<VerticalSpread> spreads = getAllSpreadCombination(options);
        SpreadAnalyzer spreadAnalyzer = new SpreadAnalyzer();

        List<SpreadAnalyzerResult> results = spreads
                .parallelStream()
                .map(s -> spreadAnalyzer.analyze(s, optionBook.getCurrentPrice(), optionBook.getNow()))
                .filter(s -> s.getExpectedTae().isPresent())
                .sorted((s1, s2) -> s2.getExpectedTae().get().compareTo(s1.getExpectedTae().get()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    private List<VerticalSpread> getAllSpreadCombination(List<Option> options) {
        SpreadFactory spreadFactory = new SpreadFactory();
        List<VerticalSpread> result = new ArrayList<>();
        List<Option> filteredOptions = options.stream().filter(o -> o.getBid().doubleValue() > 0.1 && o.getAsk().doubleValue() > 0.1 && o.getDaysToExpiry() > 0.1).collect(Collectors.toList());
        for (int i = 0; i < filteredOptions.size(); i++) {
            for (int j = 0; j < filteredOptions.size(); j++) {
                try {
                    VerticalSpread verticalSpread = spreadFactory.verticalSpread(filteredOptions.get(i), filteredOptions.get(j), 1);
                    if (verticalSpread.getMaxGain().compareTo(BigDecimal.ZERO) > 0) {
                        result.add(verticalSpread);
                    }
                } catch (OptionsException e) {
                    //Nothing to do
                }
            }
        }
        return result;
    }





    //TODO the analyzer is not having into consideration the increase or decrease of impliedVolatility
    public SpreadAnalyzerResult analyze(OptionSpread spread, BigDecimal assetPrice, LocalDate now) {
        long daysToExpiry = ChronoUnit.DAYS.between(now, spread.getExpirationDate());
        BigDecimal maxExpectedPrice = spread.getStrikePriceAverage().multiply(DISTANCE_FROM_AVERAGE_STRIKE);
        BigDecimal minExpectedPrice = spread.getStrikePriceAverage().divide(DISTANCE_FROM_AVERAGE_STRIKE, 4, RoundingMode.HALF_UP);
        BigDecimal incrementStep = maxExpectedPrice.subtract(minExpectedPrice).divide(STEPS, 4, RoundingMode.HALF_UP);

        BigDecimal currentPrice = minExpectedPrice;
        BigDecimal expirationValueAtPreviousPrice = null;
        BigDecimal ponderatedValue = BigDecimal.ZERO;
        BigDecimal maxExpectedLoss = BigDecimal.ZERO;
        BigDecimal winProbability = BigDecimal.ZERO;
        BigDecimal averageWin = BigDecimal.ZERO;
        BigDecimal averageLoss = BigDecimal.ZERO;
        List<BigDecimal> cutPoints = new ArrayList<>();

        ProbabilityDistributionService probabilityService = new ProbabilityDistributionService(assetPrice.doubleValue(), spread.getVolatility(), Math.toIntExact(daysToExpiry));
        while (currentPrice.compareTo(maxExpectedPrice) < 0d) {
            //TODO Volatility should not be the spread, but the current
            final BigDecimal probability = BigDecimal.valueOf(probabilityService.calculate(currentPrice.doubleValue(), currentPrice.add(incrementStep).doubleValue()));

            if (probability.compareTo(BigDecimal.ZERO) > 0) {
                final BigDecimal expirationValueAtCurrentPrice = spread.getExpirationValue(currentPrice);
                if(expirationValueAtCurrentPrice.compareTo(maxExpectedLoss) < 0) {
                    maxExpectedLoss = maxExpectedLoss.min(expirationValueAtCurrentPrice);
                }
                if (expirationValueAtCurrentPrice.compareTo(BigDecimal.ZERO) > 0) {
                    winProbability = winProbability.add(probability);
                    averageWin = averageWin.add(probability.multiply(expirationValueAtCurrentPrice));
                } else {
                    averageLoss = averageLoss.add(probability.multiply(expirationValueAtCurrentPrice.abs()));
                }
                if (expirationValueAtPreviousPrice != null) {
                    if (expirationValueAtCurrentPrice.compareTo(BigDecimal.ZERO) > 0 && expirationValueAtPreviousPrice.compareTo(BigDecimal.ZERO) < 0) {
                        cutPoints.add(currentPrice);
                    } else if (expirationValueAtCurrentPrice.compareTo(BigDecimal.ZERO) < 0 && expirationValueAtPreviousPrice.compareTo(BigDecimal.ZERO) > 0) {
                        cutPoints.add(currentPrice);
                    }
                }
                ponderatedValue = ponderatedValue.add(expirationValueAtCurrentPrice.multiply(probability));
                if (probability.compareTo(BigDecimal.valueOf(0.0001)) > 0) {
                    logger.debug(String.format("Probability: %.2f%%, Value: %.2f, Ponderated: %.4f", probability.multiply(BigDecimal.valueOf(100)), expirationValueAtCurrentPrice, expirationValueAtCurrentPrice.multiply(probability)));
                }
                expirationValueAtPreviousPrice = expirationValueAtCurrentPrice;
            }
            currentPrice = currentPrice.add(incrementStep);
        }


        final BigDecimal tae = tae(ponderatedValue, maxExpectedLoss, daysToExpiry);
        if (averageLoss.compareTo(BigDecimal.ZERO) == 0) {
            return new SpreadAnalyzerResult(spread, BigDecimal.ONE, null, null, null, null, Collections.emptyList());
        }
        return new SpreadAnalyzerResult(spread, winProbability, averageWin.divide(averageLoss, 4, RoundingMode.HALF_UP), tae, averageWin, averageLoss, cutPoints);
    }

    private BigDecimal tae(BigDecimal expectedGain, BigDecimal maxExpectedLoss, long daysToExpiry) {
        if (maxExpectedLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        return expectedGain.divide(maxExpectedLoss.abs(), 8, RoundingMode.HALF_UP).multiply(DAYS_OF_YEAR).divide(BigDecimal.valueOf(daysToExpiry),8, RoundingMode.HALF_UP);
    }


    private static BigDecimal calculateProbability(BigDecimal price, double sigma, long days, BigDecimal low, BigDecimal up) {
        double mu = -0.5 * sigma * sigma;
        double time = days / 365d;
        double denom = sigma * Math.sqrt(time);
        final double zlb = Math.log(low.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        final double zub = Math.log(up.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        return BigDecimal.valueOf(cumulativeDistribution(zub) - cumulativeDistribution(zlb));
    }

}
