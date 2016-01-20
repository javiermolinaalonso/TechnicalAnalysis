package com.assets.options;

import com.assets.entities.Candlestick;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PutCalculator {

    private static final double DAYS_OF_YEAR = 252d;

    public double getPutPrice(List<Candlestick> history, LocalDate expirationTime, Double strike) {
        double volatility = getAnnualizedVolatility(history, 22);
        LocalDate currentDate = LocalDateTime.ofInstant(history.get(history.size() - 1).getInitialInstant(), ZoneId.of("UTC")).toLocalDate();
        double currentPrice = history.get(history.size() - 1).getFinalPrice().doubleValue();
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
        return put;
    }

    private double getAnnualizedVolatility(List<Candlestick> history, int periods) {
        StandardDeviation standardDeviation = new StandardDeviation();

        double[] endValues = history
                .stream()
                .mapToDouble(candlestick -> candlestick.getFinalPrice().doubleValue())
                .toArray();

        double[] percentDifferences = new double[endValues.length - 1];
        for (int i = 1; i < endValues.length; i++) {
            percentDifferences[i - 1] = (endValues[i] - endValues[i - 1]) / endValues[i - 1];
        }

        double stdev = standardDeviation.evaluate(ArrayUtils.subarray(percentDifferences, percentDifferences.length - periods, percentDifferences.length));

        return stdev * Math.sqrt(DAYS_OF_YEAR);
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
