package com.assets.derivates;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.entities.Candlestick;
import com.assets.options.impl.OptionsCalculatorCandlestick;
import com.assets.options.impl.VolatilityCalculator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OptionsVolatility {

    public static final double VOLATILITY_START = 0.5;
    private static final double VOLATILITY_END = 0.15;
    static VolatilityCalculator volatilityCalculator = new VolatilityCalculator();
    static OptionsCalculatorCandlestick optionsCalculator = new OptionsCalculatorCandlestick(volatilityCalculator);
    static int step = 22;
    public static void main(String[] args) throws IOException {
        URL resource = OptionsVolatility.class.getClassLoader().getResource("Ibex1993-2015.csv");

        DataDailyLoaderCsv loader = new DataDailyLoaderCsv();
        List<Candlestick> values = loader.loadData(resource.getPath());
        Collections.sort(values, (x, y) -> x.getInitialInstant().compareTo(y.getInitialInstant()));
        VolatilityCalculator volatilityCalculator = new VolatilityCalculator();
        calculateMonthlyVolatilities(volatilityCalculator, values);
    }

    private static void calculateMonthlyVolatilities(VolatilityCalculator volatilityCalculator, List<Candlestick> values) {
        double strategyBenefit = 0;
        for(int i = step; i < values.size(); i+=5) {
            LinkedList<Candlestick> currentValues = getValues(values, i - step, i);
            double volatility = volatilityCalculator.getAnnualizedVolatility(currentValues);
            if(volatility > VOLATILITY_START) {
                double performance = doEvaluatePutSell(values, i, currentValues);
                strategyBenefit+= performance;
            }
        }
        System.out.println(String.format("Final benefit is %.2f", strategyBenefit));
    }

    private static double doEvaluatePutSell(List<Candlestick> values, int index, LinkedList<Candlestick> currentValues) {
        Candlestick candlestick = values.get(index);
        LocalDate strikeDate = candlestick.getDate().plusMonths(12);
        double strikePrice = candlestick.getFinalPrice().multiply(BigDecimal.valueOf(0.9)).doubleValue();
        double price = getPrice(values, index, strikeDate, strikePrice, candlestick);
        double finalPrice = evaluatePerformance(values, index, strikeDate, strikePrice);
        System.out.println(String.format("Operation profit: %.2f", price - finalPrice));
        return price - finalPrice;
    }

    private static double evaluatePerformance(List<Candlestick> values, int index, LocalDate strikeDate, double strikePrice) {
        Candlestick candlestick = values.get(index);
        int days;
        double volatility;
        double currentPrice;
        do {
            days = getDays(strikeDate, candlestick.getDate());
            volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - days, index));
            currentPrice = optionsCalculator.put(candlestick, strikeDate, strikePrice, volatility);
            index++;
            candlestick = values.get(index);
        } while(candlestick.getDate().isBefore(strikeDate) && index < values.size() && volatility > VOLATILITY_END);
        return currentPrice;
    }

    private static double getPrice(List<Candlestick> values, int index, LocalDate strikeDate, double strikePrice, Candlestick candlestick) {
        int days = getDays(strikeDate, candlestick.getDate());
        double volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - days, index));
        return optionsCalculator.put(candlestick, strikeDate, strikePrice, volatility);
    }

    private static int getDays(LocalDate strikeDate, LocalDate startDate) {
        Period between = Period.between(startDate, strikeDate);
        return between.getDays() + between.getMonths() * 22 + between.getYears() * 252;
    }

    private static double sellPut(LinkedList<Candlestick> currentValues, LocalDate strikeDate, double strikePrice) {
        return optionsCalculator.put(currentValues, strikeDate, strikePrice);
    }

    private static LinkedList<Candlestick> getValues(List<Candlestick> candlesticks, int minIndex, int maxIndex) {
        LinkedList<Candlestick> values = new LinkedList<>();
        for(int i = minIndex; i < maxIndex; i++) {
            values.addLast(candlesticks.get(i));
        }
        return values;
    }
}
