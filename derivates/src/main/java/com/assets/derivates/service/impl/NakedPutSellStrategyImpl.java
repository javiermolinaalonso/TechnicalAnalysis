package com.assets.derivates.service.impl;

import com.assets.derivates.entities.NakedPutVolatilityStrategyResult;
import com.assets.derivates.service.NakedPutSellStrategy;
import com.assets.entities.Candlestick;
import com.assets.options.impl.OptionsCalculatorCandlestick;
import com.assets.options.impl.VolatilityCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

public class NakedPutSellStrategyImpl implements NakedPutSellStrategy<Candlestick> {

    private final VolatilityCalculator volatilityCalculator;
    private final OptionsCalculatorCandlestick optionsCalculator;

    public NakedPutSellStrategyImpl(VolatilityCalculator volatilityCalculator, OptionsCalculatorCandlestick optionsCalculator) {
        this.volatilityCalculator = volatilityCalculator;
        this.optionsCalculator = optionsCalculator;
    }

    public NakedPutVolatilityStrategyResult calculateStrategy(List<Candlestick> values, int step, double volatilityStart, double strikeDistance, double volatilityEnd) {
        double strategyBenefit = 0;
        int operations = 0;
        for(int i = step; i < values.size(); i+=5) {
            LinkedList<Candlestick> currentValues = getValues(values, i - step, i);
            double volatility = volatilityCalculator.getAnnualizedVolatility(currentValues);
            if(volatility > volatilityStart) {
                double performance = doEvaluatePutSell(values, i, currentValues, strikeDistance, volatilityEnd);
                strategyBenefit+= performance;
                operations++;
            }
        }
        return new NakedPutVolatilityStrategyResult.Builder()
                .withResult(strategyBenefit)
                .withStrikeDistance(strikeDistance)
                .withVolatilityStart(volatilityStart)
                .withVolatilityEnd(volatilityEnd)
                .withOperations(operations)
                .build();
    }

    private double doEvaluatePutSell(List<Candlestick> values, int index, LinkedList<Candlestick> currentValues, double strikeDistance, double volatilityEnd) {
        Candlestick candlestick = values.get(index);
        LocalDate strikeDate = candlestick.getDate().plusMonths(12);
        double strikePrice = candlestick.getFinalPrice().multiply(BigDecimal.valueOf(strikeDistance)).doubleValue();
        double price = getPrice(values, index, strikeDate, strikePrice, candlestick);
        double finalPrice = evaluatePerformance(values, index, strikeDate, strikePrice, volatilityEnd);
        return price - finalPrice;
    }

    private double evaluatePerformance(List<Candlestick> values, int index, LocalDate strikeDate, double strikePrice, double volatilityEnd) {
        Candlestick candlestick = values.get(index);
        int days;
        double volatility;
        double currentPrice;
        do {
            days = getDays(strikeDate, candlestick.getDate());
            volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - Math.max(days, 10), index));
            currentPrice = optionsCalculator.put(candlestick, strikeDate, strikePrice, volatility);
            index++;
            candlestick = values.get(index);
        } while(candlestick.getDate().isBefore(strikeDate) && index < values.size()-1 && volatility > volatilityEnd);
        return currentPrice;
    }

    private double getPrice(List<Candlestick> values, int index, LocalDate strikeDate, double strikePrice, Candlestick candlestick) {
        int days = getDays(strikeDate, candlestick.getDate());
        double volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - days, index));
        return optionsCalculator.put(candlestick, strikeDate, strikePrice, volatility);
    }

    private int getDays(LocalDate strikeDate, LocalDate startDate) {
        Period between = Period.between(startDate, strikeDate);
        return between.getDays() + between.getMonths() * 22 + between.getYears() * 252;
    }

    private double sellPut(LinkedList<Candlestick> currentValues, LocalDate strikeDate, double strikePrice) {
        return optionsCalculator.put(currentValues, strikeDate, strikePrice);
    }

    private LinkedList<Candlestick> getValues(List<Candlestick> candlesticks, int minIndex, int maxIndex) {
        LinkedList<Candlestick> values = new LinkedList<>();
        for(int i = minIndex; i < maxIndex; i++) {
            values.addLast(candlesticks.get(i));
        }
        return values;
    }
}
