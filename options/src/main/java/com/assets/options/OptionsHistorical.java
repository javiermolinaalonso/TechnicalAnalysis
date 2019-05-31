package com.assets.options;

import com.assets.entities.StockPrice;
import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.analyzers.SpreadAnalyzerResult;
import com.assets.options.entities.spread.IronCondorSpread;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.statistic.list.LambdaStatisticList;
import com.assets.statistic.list.StockList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class OptionsHistorical {

    public static final String SPX = "SPX";
    public static final int DAYS_CALCULATION = 30;
//    public static final int DAYS_COUNT = 7058;
    public static final int DAYS_COUNT = 30;
    public static final double RISK_FREE = 0.025d;
    private static final double BUY_PERCENTAGE = 0.07d;
    private static final double SELL_PERCENTAGE = 0.05d;
    public static final int MIN_BENEFIT = 20;


    public static void main(String[] args) throws IOException, ParseException {
        final StockList prices = readData("/dailyprices_SPX_2019.csv");

        for (int i = DAYS_COUNT; i < prices.size(); i++) {
            final StockPrice currentPrice = prices.get(i);
            final LambdaStatisticList statisticList = new LambdaStatisticList(prices.subList(i - DAYS_CALCULATION, i - 1).stream().map(StockPrice::getValue).collect(Collectors.toList()));

            final BigDecimal stdDev = statisticList.getStdDev().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
            final double value = currentPrice.getValue().doubleValue();
            final Optional<Pair<SpreadAnalyzerResult, OptionSpread>> spreadOptional = simulateIronCondor(value,
                    stdDev.doubleValue(),
                    currentPrice.getLocalDate()
            );

            if (spreadOptional.isPresent()) {
//                i = printResult(prices, i, spreadOptional.get());
                i = computeStrategyAndPrintResult(prices, i, spreadOptional.get());
            }
        }
    }

    private static int computeStrategyAndPrintResult(StockList prices, int i, Pair<SpreadAnalyzerResult, OptionSpread> spreadWithResults) {
        System.out.println("Expectation: " + spreadWithResults.getKey().toString());
        System.out.println(String.format("Purchased: %s purchased at %s with current price %.2f", spreadWithResults.getValue().toString(), prices.get(i).getLocalDate(), prices.get(i).getValue()));
        int j = i + 1;
        OptionSpread spread = spreadWithResults.getValue();
        final LocalDate startingDate = prices.get(j-1).getLocalDate();
        while (j < prices.size() && prices.get(j).getLocalDate().isBefore(spread.getExpirationDate())) {
            final StockPrice stockPrice = prices.get(j);
            final BigDecimal currentSpreadValue = spread.getValueAt(stockPrice.getValue(), stockPrice.getLocalDate());
            long passedDays = ChronoUnit.DAYS.between(startingDate, stockPrice.getLocalDate());

            BigDecimal expectedTae = currentSpreadValue.divide(spreadWithResults.getValue().getMaxLoss().abs(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(passedDays), 4, RoundingMode.HALF_UP);
            if (expectedTae.compareTo(spreadWithResults.getKey().getExpectedTae().get()) > 0 && currentSpreadValue.compareTo(BigDecimal.valueOf(MIN_BENEFIT)) > 0) {
                return printAndReturn(j, spread, stockPrice, prices.get(i).getLocalDate());
            }
            j++;
        }
        final StockPrice stockPrice = prices.get(j - 1);
        return printAndReturn(j, spread, stockPrice, prices.get(i).getLocalDate());
    }

    private static int printAndReturn(int j, OptionSpread spread, StockPrice stockPrice, LocalDate spreadDate) {
        final BigDecimal value = spread.getValueAt(stockPrice.getValue(), stockPrice.getLocalDate());
        System.out.println(String.format("Result=%.2f, currentStockPrice=%.2f, date=%s", value, stockPrice.getValue(), stockPrice.getLocalDate()));
        System.out.println(String.format("%.2f in %d days", value, Period.between(spreadDate, stockPrice.getLocalDate()).getDays()));
        System.out.println("****************************************");
        return j;
    }

    private static int printResult(StockList prices, int i, Pair<SpreadAnalyzerResult, OptionSpread> spreadWithResults) {
        System.out.println("Expectation: " + spreadWithResults.getKey().toString());
        System.out.println(String.format("Purchased: %s purchased at %s with current price %.2f", spreadWithResults.getValue().toString(), prices.get(i).getLocalDate(), prices.get(i).getValue()));
        int j = i + 1;
        OptionSpread spread = spreadWithResults.getValue();
        while (j < prices.size() && prices.get(j).getLocalDate().isBefore(spread.getExpirationDate())) {
            j++;
        }
        final StockPrice lastStockPrice = prices.get(j - 1);
        System.out.println(String.format("Result=%.2f, currentStockPrice=%.2f", spread.getValueAt(lastStockPrice.getValue(), lastStockPrice.getLocalDate()), lastStockPrice.getValue()));
        System.out.println("****************************************");
        return j;
    }

    private static Optional<Pair<SpreadAnalyzerResult, OptionSpread>> simulateIronCondor(double currentPrice, double volatility, LocalDate now) {
        Pair<SpreadAnalyzerResult, OptionSpread> pair = null;
        SpreadAnalyzer spreadAnalyzer = new SpreadAnalyzer();

        for (int daysToExpiry = 10; daysToExpiry < 90; daysToExpiry += 5) {
            for (double putLowP = 0.05d; putLowP < 0.10d; putLowP += 0.01d) {
                for (double putHiP = 0.01d; putHiP < putLowP; putHiP += 0.01d) {
                    for (double callLoP = 0.01d; callLoP < 0.05d; callLoP += 0.01d) {
                        for (double callHiP = callLoP + 0.01d; callHiP < 0.1d; callHiP += 0.01d) {
                            final int lowLossStrike = BigDecimal.valueOf(currentPrice - currentPrice * putLowP).setScale(0, RoundingMode.DOWN).intValue();
                            final int lowProfitStrike = BigDecimal.valueOf(currentPrice - currentPrice * putHiP).setScale(0, RoundingMode.UP).intValue();
                            final int highProfitStrike = BigDecimal.valueOf(currentPrice + currentPrice * callLoP).setScale(0, RoundingMode.DOWN).intValue();
                            final int highLossStrike = BigDecimal.valueOf(currentPrice + currentPrice * callHiP).setScale(0, RoundingMode.UP).intValue();

                            final IronCondorSpread spread = new IronCondorSpread(currentPrice, lowLossStrike, volatility, lowProfitStrike, volatility, highProfitStrike, volatility, highLossStrike, volatility, now, now.plusDays(daysToExpiry), volatility, RISK_FREE, BigDecimal.ONE, SPX, 1, false);
                            if (spread.isValid()) {
                                final SpreadAnalyzerResult analyzerResult = spreadAnalyzer.analyze(spread, BigDecimal.valueOf(currentPrice), now);
//                                System.out.println(analyzerResult);
                                if (analyzerResult.getExpectedTae().get().compareTo(BigDecimal.valueOf(0)) > 0) {
                                    if (pair == null || analyzerResult.getExpectedTae().get().compareTo(pair.getKey().getExpectedTae().get()) > 0) {
                                        pair = new ImmutablePair<>(analyzerResult, spread);
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
