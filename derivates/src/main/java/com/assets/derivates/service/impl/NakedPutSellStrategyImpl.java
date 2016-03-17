package com.assets.derivates.service.impl;

import com.assets.derivates.strategies.NakedPutVolatilityStrategyResult;
import com.assets.derivates.entities.PutResult;
import com.assets.derivates.service.NakedPutSellStrategy;
import com.assets.entities.Candlestick;
import com.assets.options.impl.OptionsCalculatorCandlestick;
import com.assets.options.impl.VolatilityCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
        List<PutResult> results = new ArrayList<>();
        for (int i = step; i < values.size(); i += 10) {
            double volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, i - step, i));
            if (volatility > volatilityStart) {
                PutResult.Builder result = doEvaluatePutSell(values, i, strikeDistance, volatilityEnd);
                PutResult putResult = result.withInitialVolatility(volatility).build();
                strategyBenefit += putResult.getBenefit();
                results.add(putResult);
            }
        }
        return new NakedPutVolatilityStrategyResult.Builder()
                .withResult(strategyBenefit)
                .withStrikeDistance(strikeDistance)
                .withVolatilityStart(volatilityStart)
                .withVolatilityEnd(volatilityEnd)
                .withResults(results)
                .build();
    }

    private PutResult.Builder doEvaluatePutSell(List<Candlestick> values, int index, double strikeDistance, double volatilityEnd) {
        Candlestick candlestick = values.get(index);
        LocalDate strikeDate = candlestick.getDate().plusMonths(12);
        BigDecimal strikePrice = candlestick.getFinalPrice().multiply(BigDecimal.valueOf(strikeDistance));
        BigDecimal initialPremium = getPrice(values, index, strikeDate, strikePrice, candlestick);
        PutResult.Builder builder = evaluatePerformance(values, index, strikeDate, strikePrice, volatilityEnd);
        return builder
                .withInitialPremium(initialPremium.doubleValue())
                .withStrikePrice(strikePrice.doubleValue())
                .withInitialPrice(candlestick.getFinalPrice().doubleValue())
                .withStartDate(candlestick.getDate())
                .withStrikeDate(strikeDate);
    }

    private PutResult.Builder evaluatePerformance(List<Candlestick> values, int index, LocalDate strikeDate, BigDecimal strikePrice, double volatilityEnd) {
        Candlestick candlestick = values.get(index);
        double maxPremium = 0d;
        double maxVolatility = 0d;
        double volatility;
        double currentPremium;
        do {
            int days = getDays(strikeDate, candlestick.getDate());
            volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - Math.max(days, 10), index));
            currentPremium = optionsCalculator.put(candlestick, strikeDate, candlestick.getDate(), strikePrice, volatility).getPremium().doubleValue();
            maxPremium = Math.max(currentPremium, maxPremium);
            maxVolatility = Math.max(volatility, maxVolatility);
            index++;
            candlestick = values.get(index);
        } while (candlestick.getDate().isBefore(strikeDate) && index < values.size() - 1 && volatility > volatilityEnd);

        return new PutResult.Builder()
                .withEndDate(candlestick.getDate())
                .withMaxPremium(maxPremium)
                .withMaxVolatility(maxVolatility)
                .withFinalPremium(currentPremium);
    }

    private BigDecimal getPrice(List<Candlestick> values, int index, LocalDate strikeDate, BigDecimal strikePrice, Candlestick candlestick) {
        int days = getDays(strikeDate, candlestick.getDate());
        double volatility = volatilityCalculator.getAnnualizedVolatility(getValues(values, index - days, index));
        return optionsCalculator.put(candlestick, strikeDate, candlestick.getDate(), strikePrice, volatility).getPremium();
    }

    private int getDays(LocalDate strikeDate, LocalDate startDate) {
        Period between = Period.between(startDate, strikeDate);
        return between.getDays() + between.getMonths() * 22 + between.getYears() * 252;
    }

    private LinkedList<Candlestick> getValues(List<Candlestick> candlesticks, int minIndex, int maxIndex) {
        minIndex = Math.max(0, minIndex);
        LinkedList<Candlestick> values = new LinkedList<>();
        for (int i = minIndex; i < maxIndex; i++) {
            values.addLast(candlesticks.get(i));
        }
        return values;
    }
}
