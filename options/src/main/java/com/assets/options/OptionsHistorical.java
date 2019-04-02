package com.assets.options;

import com.assets.entities.StockPrice;
import com.assets.options.entities.Option;
import com.assets.options.entities.spread.IronCondorSpread;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.statistic.entities.StatisticList;
import com.assets.statistic.list.LambdaStatisticList;
import com.assets.statistic.list.StockList;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static net.finmath.functions.NormalDistribution.cumulativeDistribution;

public class OptionsHistorical {

    public static final String SPX = "SPX";
    public static final int DAYS_CALCULATION = 90;

    private static final double BUY_PERCENTAGE = 0.07d;
    private static final double SELL_PERCENTAGE = 0.05d;
    public static final int DAYS_COUNT = 90;
    public static final double RISK_FREE = 0.025d;
    private static double l1;
    private static double l2;
    private static double h2;
    private static double h1;


    public static void main(String[] args) throws IOException, ParseException {
        final StockList prices = readData("/dailyprices_SPX.csv");

        double results = 0d;
        for (int i = DAYS_COUNT; i < prices.size(); i++) {
            final StockPrice currentPrice = prices.get(i);
            final LambdaStatisticList statisticList = new LambdaStatisticList(prices.subList(i - DAYS_CALCULATION, i - 1).stream().map(StockPrice::getValue).collect(Collectors.toList()));

            final BigDecimal stdDev = statisticList.getStdDev().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
            final double value = currentPrice.getValue().doubleValue();
            final Optional<Pair<Double, OptionSpread>> spreadOptional = integrationValue(value,
                    stdDev.doubleValue(),
                    currentPrice.getLocalDate()
            );

            if (spreadOptional.isPresent()) {
                results += getResults(prices, i, spreadOptional);
                System.out.println(results);
                i+=45;
            }
        }
    }

    private static double getResults(StockList prices, int i, Optional<Pair<Double, OptionSpread>> spreadOptional) {
        final OptionSpread spread = spreadOptional.get().getValue();
        System.out.println(String.format("[%.2f, %.2f, %.2f, %.2f, %s] %.2f, %s, %.2f", l1, l2, h2, h1, spread.getExpirationDate().toString(), prices.get(i).getValue().doubleValue(), prices.get(i).getLocalDate(), spread.getValueAt(prices.get(i).getValue(), prices.get(i).getLocalDate())));
        int j = i + 1;
        StockPrice stockAtFuture = prices.get(j);
        while (j < prices.size() && stockAtFuture.getLocalDate().isBefore(spread.getExpirationDate())) {
            stockAtFuture = prices.get(j);
            final BigDecimal spreadValue = spread.getValueAt(stockAtFuture.getValue(), stockAtFuture.getLocalDate());
            j++;
            if (spreadValue.divide(spread.getMaxGain(), 6, RoundingMode.HALF_UP).doubleValue() > 0.3) {
                return spreadValue.doubleValue();
            }
            System.out.println(String.format("[%.2f, %.2f, %.2f, %.2f, %s] %.2f, %s, %.2f", l1, l2, h2, h1, spread.getExpirationDate().toString(), stockAtFuture.getValue().doubleValue(), stockAtFuture.getLocalDate(), spreadValue.doubleValue()));
        }
        return spread.getMaxLoss().doubleValue();
    }

    private static Optional<Pair<Double, OptionSpread>> integrationValue(double currentPrice, double volatility, LocalDate now) {
        Pair<Double, OptionSpread> pair = null;
        int maxExpectedPrice = Double.valueOf(currentPrice).intValue() * 3;

        for (int daysToExpiry = 10; daysToExpiry < 30; daysToExpiry+=5) {
            for (double putLowP = 0.05d; putLowP < 0.15d; putLowP+=0.01d) {
                for (double putHiP = 0.01d; putHiP < putLowP; putHiP+=0.01d) {
                    for (double callLoP = 0.01d; callLoP < 0.05d; callLoP += 0.01d) {
                        for (double callHiP = callLoP + 0.01d; callHiP < 0.15d; callHiP += 0.01d) {
                            final double lowLossStrike = currentPrice - currentPrice * putLowP;
                            final double lowProfitStrike = currentPrice - currentPrice * putHiP;
                            final double highProfitStrike = currentPrice + currentPrice * callLoP;
                            final double highLossStrike = currentPrice + currentPrice * callHiP;

                            final IronCondorSpread spread = new IronCondorSpread(currentPrice, lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, now, now.plusDays(daysToExpiry), volatility, RISK_FREE, BigDecimal.ONE, SPX, 1, false);

                            if (spread.getMaxGain().doubleValue() > 0) {
                                double previousPrice = 0;
                                BigDecimal finalValue = BigDecimal.ZERO;
                                double winProbability = 0d;
                                double looseProbability = 0d;

                                for (double endPrice = lowLossStrike; endPrice < maxExpectedPrice; endPrice++) {
                                    final double probability = calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.valueOf(previousPrice), BigDecimal.valueOf(endPrice));
                                    final BigDecimal expirationValue = spread.getExpirationValue(BigDecimal.valueOf(endPrice));
                                    final BigDecimal ponderatedValue = BigDecimal.valueOf(probability).multiply(expirationValue);
                                    if (expirationValue.compareTo(BigDecimal.ZERO) > 0) {
                                        winProbability += probability;
                                    } else {
                                        looseProbability += probability;
                                    }
                                    finalValue = finalValue.add(ponderatedValue);
                                    previousPrice = endPrice;
                                }
                                double tae = (finalValue.divide(spread.getMaxLoss().abs(), 8, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(daysToExpiry), 8, RoundingMode.HALF_UP).doubleValue() * 100;
                                if (tae > 1) {
//                                    System.out.println(String.format(" %d, %.0f,  %.0f, %.2f%%, %.2f%%,     %.2f, %.2f%%", daysToExpiry, spread.getMaxGain(), spread.getMaxLoss().negate(),     winProbability * 100, looseProbability * 100, finalValue, tae));
                                    if (pair == null || tae > pair.getKey()) {
                                        pair = new Pair<>(tae, spread);
                                        l1 = lowLossStrike;
                                        l2 = lowProfitStrike;
                                        h2 = highProfitStrike;
                                        h1 = highLossStrike;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Optional.ofNullable(pair);
    }

    private static double calculateProbability(BigDecimal price, double sigma, int days, BigDecimal low, BigDecimal up) {
        double mu = -0.5 * sigma * sigma;
        double time = days / 365d;
        double denom = sigma * Math.sqrt(time);
        final double zlb = Math.log(low.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        final double zub = Math.log(up.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        return cumulativeDistribution(zub) - cumulativeDistribution(zlb);
    }

    private static StockList readData(String file) throws IOException, ParseException {
        final InputStream spx = OptionsHistorical.class.getResourceAsStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(spx));
        StockList stockList = new StockList("SPX");
        String line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            final String[] values = line.split(",");
            LocalDate when = LocalDate.parse(values[0]);
            Number value = new DecimalFormat().parse(values[5]);
            stockList.add(new StockPrice("SPX", when, value));
            line = reader.readLine();
        }
        return stockList;
    }
}
