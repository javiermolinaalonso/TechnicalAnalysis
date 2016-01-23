package com.assets.options.impl;

import com.assets.entities.Candlestick;
import com.assets.options.OptionsCalculator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OptionsCalculatorCandlestick implements OptionsCalculator<Candlestick> {

    private static final double DAYS_OF_YEAR = 252d;

    private final VolatilityCalculator volatilityCalculator;

    public OptionsCalculatorCandlestick(VolatilityCalculator volatilityCalculator) {
        this.volatilityCalculator = volatilityCalculator;
    }

    @Override
    public double call(List<Candlestick> values, LocalDate expiration, double strike) {
        double volatility = volatilityCalculator.getAnnualizedVolatility(values);
        return blackScholes(values.get(values.size() - 1), expiration, strike, volatility)[0];
    }

    @Override
    public double call(Candlestick value, LocalDate expiration, double strike, double volatility) {
        return blackScholes(value, expiration, strike, volatility)[0];
    }

    @Override
    public double put(Candlestick value, LocalDate expiration, double strike, double volatility) {
        return blackScholes(value, expiration, strike, volatility)[1];
    }

    public double put(List<Candlestick> values, LocalDate expiration, double strike) {
        double volatility = volatilityCalculator.getAnnualizedVolatility(values);
        return blackScholes(values.get(values.size() - 1), expiration, strike, volatility)[1];
    }

    private double[] blackScholes(Candlestick value, LocalDate expirationTime, double strike, double volatility) {
        LocalDate currentDate = value.getDate();
        double currentPrice = value.getFinalPrice().doubleValue();
        double tax = 0.02;

        double years = (double) ChronoUnit.DAYS.between(currentDate, expirationTime) / DAYS_OF_YEAR;

        double actualValue = strike * Math.pow(Math.E, years * -1 * tax);
        double sigma = volatility * Math.pow(years, 0.5);
        double d1 = (Math.log(currentPrice / strike) + (tax + Math.pow(volatility, 2) / 2) * years) / (volatility * Math.pow(years, 0.5));
        double d2 = d1 - sigma;
        double delta = normdist(d1);
        double warrant = normdist(d2) * actualValue;
        double call = delta * currentPrice - warrant;
        double put = call + actualValue - currentPrice;
        return new double[]{call, put};
    }

    double normdist(double x) {
        int neg = (x < 0d) ? 1 : 0;
        if (neg == 1)
            x *= -1d;

        double k = (1d / (1d + 0.2316419 * x));
        double y = ((((1.330274429 * k - 1.821255978) * k + 1.781477937) *
                k - 0.356563782) * k + 0.319381530) * k;
        y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;

        return (1d - neg) * y + neg * (1d - y);
    }
}
