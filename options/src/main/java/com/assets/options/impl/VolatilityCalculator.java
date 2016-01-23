package com.assets.options.impl;

import com.assets.entities.Candlestick;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.List;

public class VolatilityCalculator {

    private static final double DAYS_OF_YEAR = 252d;

    public double getAnnualizedVolatility(List<Candlestick> history) {
        StandardDeviation standardDeviation = new StandardDeviation();

        double[] endValues = history
                .stream()
                .mapToDouble(candlestick -> candlestick.getFinalPrice().doubleValue())
                .toArray();

        double[] percentDifferences = new double[endValues.length - 1];
        for (int i = 1; i < endValues.length; i++) {
            percentDifferences[i - 1] = (endValues[i] - endValues[i - 1]) / endValues[i - 1];
        }

        double stdev = standardDeviation.evaluate(percentDifferences);

        return stdev * Math.sqrt(DAYS_OF_YEAR);
    }
}
