package com.assets.options;

import org.apache.commons.lang3.tuple.Pair;

public class SuccessRatePrinter {

    public static void main(String[] args) {
        double currentPrice = 90;
        for (int i = 10; i < 365; i+=10) {
            final double expectedPrice = currentPrice + currentPrice * 0.1 * i / 365;
            final Pair<Double, Double> successRange = getSuccessRange(currentPrice, 0.3, i, expectedPrice);
            System.out.println(String.format("[%8.2f, %8.2f]", successRange.getLeft(), successRange.getRight()));
        }
    }

    private static Pair<Double, Double> getSuccessRange(double currentPrice, double volatility, int daysToExpire) {
        return getSuccessRange(currentPrice, volatility, daysToExpire, currentPrice);
    }

    private static Pair<Double, Double> getSuccessRange(double currentPrice, double volatility, int daysToExpire, double expectation) {
        final double range = currentPrice * volatility * Math.sqrt(daysToExpire / 365d);
        return Pair.of(expectation - range, expectation + range);
    }
}
