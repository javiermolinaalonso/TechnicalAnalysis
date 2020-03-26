package com.assets.options.analyzers;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Option;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.exceptions.OptionsException;
import com.assets.options.entities.spread.vertical.VerticalSpread;
import org.apache.commons.lang3.Range;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;

public class SpreadAnalyzer {

    public static final BigDecimal DISTANCE_FROM_AVERAGE_STRIKE = valueOf(1.2);
    public static final BigDecimal DAYS_OF_YEAR = valueOf(365);

    public List<SpreadAnalyzerResult> analyzeByDate(OptionBook book) {
        return book.getAvailableDates()
                .stream()
                .map(expirationDAte -> getBestVerticalSpread(expirationDAte, book))
                .filter(t -> t.isPresent())
                .map(t -> t.get())
                .collect(Collectors.toList());
    }

    private Range<BigDecimal> getRange(OptionBook optionBook) {
        return Range.between(optionBook.getCurrentPrice().multiply(DISTANCE_FROM_AVERAGE_STRIKE), optionBook.getCurrentPrice().divide(DISTANCE_FROM_AVERAGE_STRIKE, 2, RoundingMode.HALF_UP));
    }

    private Optional<SpreadAnalyzerResult> getBestVerticalSpread(LocalDate expirationDate, OptionBook optionBook) {
        List<Option> options = optionBook.getOptions(expirationDate);
        Range<BigDecimal> range = getRange(optionBook);
        List<VerticalSpread> spreads = getAllSpreadCombination(options, range);

        BigDecimal expectedPrice = optionBook.getCurrentPrice(); //By default let's set the expected price is the current price
        double volatility = optionBook.getVolatility(expirationDate);
        long daysToExpiry = ChronoUnit.DAYS.between(optionBook.getNow(), expirationDate);
        ProbabilityDistributionService probabilityService = new ProbabilityDistributionService(expectedPrice, volatility, Math.toIntExact(daysToExpiry));
        VerticalSpreadAnalyzer spreadAnalyzer = new VerticalSpreadAnalyzer(range, optionBook.getNow(), optionBook.getCurrentPrice(), probabilityService);

        List<SpreadAnalyzerResult> results = spreads
                .parallelStream()
                .map(s -> spreadAnalyzer.analyze(s))
                .filter(s -> s.getExpectedTae().isPresent())
                .filter(s -> s.getWinProbability().isPresent() && Range.between(valueOf(0.4), valueOf(0.6)).contains(s.getWinProbability().get()))
//                .filter(s -> s.getWinProbability().isPresent() && s.getWinProbability().get().compareTo(BigDecimal.valueOf(0.2)) > 0)
                .sorted((s1, s2) -> s2.getExpectedTae().get().compareTo(s1.getExpectedTae().get()))
                .collect(Collectors.toList());
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    private List<VerticalSpread> getAllSpreadCombination(List<Option> options, Range<BigDecimal> range) {
        SpreadFactory spreadFactory = new SpreadFactory();
        List<VerticalSpread> result = new ArrayList<>();
        List<Option> filteredOptions = options.stream()
                .filter(o -> o.isTradeable())
                .filter(o -> range.contains(o.getStrikePrice()))
                .collect(Collectors.toList());
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


}
