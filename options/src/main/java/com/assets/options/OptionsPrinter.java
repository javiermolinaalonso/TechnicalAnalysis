package com.assets.options;

import com.assets.options.entities.spread.IronCondorSpread;
import com.assets.options.entities.spread.OptionSpread;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static net.finmath.functions.NormalDistribution.cumulativeDistribution;

public class OptionsPrinter {


    private static final double currentPrice = 282;

//    private static final double lowLossStrike = 250;
//    private static final double lowProfitStrike = 255;
//    private static final double highProfitStrike = 265;
//    private static final double highLossStrike = 270;
    private static final double lowLossStrike = 260;
    private static final double lowProfitStrike = 265;
    private static final double highProfitStrike = 275;
    private static final double highLossStrike = 280;
//    private static final double lowLossStrike = 285;
//    private static final double lowProfitStrike = 290;
//    private static final double highProfitStrike = 300;
//    private static final double highLossStrike = 305;
    private static final double impliedVolatility = 0.15d;
    private static final double volatility = 0.106d;

    public static void main(String[] args) {
//        simpleValuationValue(currentPrice, lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, impliedVolatility);
        integrationValue(currentPrice, lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, impliedVolatility, volatility, LocalDate.now());
        System.exit(0);
    }


    private static Optional<Pair<Double, OptionSpread>> integrationValue(double currentPrice, double lowLossStrike, double lowProfitStrike, double highProfitStrike, double highLossStrike, double impliedVolatility, double volatility, LocalDate now) {
        System.out.println("Days, gain, loss, win%, loss%, expectedGain, expectedLoss, TAE");
        Pair<Double, OptionSpread> pair = null;
        int maxExpectedPrice = Double.valueOf(highLossStrike).intValue() * 3;
        for (int daysToExpiry = 5; daysToExpiry < 180; daysToExpiry+=5) {
            final IronCondorSpread ironCondor = new IronCondorSpread(currentPrice, lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, now, now.plusDays(daysToExpiry), volatility, 0.02d, BigDecimal.ONE, "SPX", 1, false);

            double previousPrice = 0;
            BigDecimal finalValue = BigDecimal.ZERO;
            double winProbability = 0d;
            double looseProbability = 0d;

            for (double endPrice = lowLossStrike; endPrice < maxExpectedPrice; endPrice++) {
                final double probability = calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.valueOf(previousPrice), BigDecimal.valueOf(endPrice));
                final BigDecimal expirationValue = ironCondor.getExpirationValue(BigDecimal.valueOf(endPrice));
                final BigDecimal ponderatedValue = BigDecimal.valueOf(probability).multiply(expirationValue);
                if (expirationValue.compareTo(BigDecimal.ZERO) > 0) {
                    winProbability += probability;
                } else {
                    looseProbability += probability;
                }
                finalValue = finalValue.add(ponderatedValue);
                previousPrice = endPrice;
            }
            double tae = (finalValue.divide(ironCondor.getMaxLoss().abs(), 8, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(daysToExpiry), 8, RoundingMode.HALF_UP).doubleValue() * 100;
            System.out.println(String.format(" %d, %.0f,  %.0f, %.2f%%, %.2f%%,     %.2f, %.2f%%", daysToExpiry, ironCondor.getMaxGain(), ironCondor.getMaxLoss().negate(), winProbability*100, looseProbability*100, finalValue, tae));
            if (tae > 0) {
                if (pair == null || tae > pair.getKey()) {
                    pair = new Pair<>(tae, ironCondor);
                }
            }
        }
        return Optional.ofNullable(pair);
    }

    //possible variables...
    // implied volatility, volatility, days to expiry, strike prices
    private static void integrationValue(double currentPrice, double lowLossStrike, double lowProfitStrike, double highProfitStrike, double highLossStrike, double impliedVolatility, double volatility) {
        System.out.println("Days, gain, loss, win%, loss%, expectedGain, expectedLoss, TAE");
        int maxExpectedPrice = Double.valueOf(highLossStrike).intValue() * 3;
        for (int daysToExpiry = 5; daysToExpiry < 90; daysToExpiry+=5) {
            final IronCondorSpread ironCondor = IronCondorSpread.basicIronCondor(currentPrice,lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, daysToExpiry, impliedVolatility, "SPY");

            double previousPrice = 0;
            BigDecimal finalValue = BigDecimal.ZERO;
            double winProbability = 0d;
            double looseProbability = 0d;

            for (double endPrice = lowLossStrike; endPrice < maxExpectedPrice; endPrice++) {
                final double probability = calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.valueOf(previousPrice), BigDecimal.valueOf(endPrice));
                final BigDecimal expirationValue = ironCondor.getExpirationValue(BigDecimal.valueOf(endPrice));
                final BigDecimal ponderatedValue = BigDecimal.valueOf(probability).multiply(expirationValue);
                if (expirationValue.compareTo(BigDecimal.ZERO) > 0) {
                    winProbability += probability;
                } else {
                    looseProbability += probability;
                }
                finalValue = finalValue.add(ponderatedValue);
                previousPrice = endPrice;
            }
            double tae = (finalValue.divide(ironCondor.getMaxLoss().abs(), 8, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(daysToExpiry), 8, RoundingMode.HALF_UP).doubleValue() * 100;
            System.out.println(String.format(" %d, %.0f,  %.0f, %.2f%%, %.2f%%,     %.2f, %.2f%%", daysToExpiry, ironCondor.getMaxGain(), ironCondor.getMaxLoss().negate(), winProbability*100, looseProbability*100, finalValue, tae));
        }

    }

    private static void simpleValuationValue(double currentPrice, double lowLossStrike, double lowProfitStrike, double highProfitStrike, double highLossStrike, double volatility) {
        System.out.println("Days, gain, loss, win%, loss%, expectedGain, expectedLoss, ratio");
        for (int daysToExpiry = 10; daysToExpiry < 90; daysToExpiry+=5) {
            final IronCondorSpread ironCondor =
                    IronCondorSpread.basicIronCondor(currentPrice,lowLossStrike, lowProfitStrike, highProfitStrike, highLossStrike, daysToExpiry, volatility, "SPY");

            final double maxWinProbability = calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.valueOf(lowProfitStrike), BigDecimal.valueOf(highProfitStrike));
            final double maxLossProbability = calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.ZERO, BigDecimal.valueOf(lowLossStrike))
                    +
                    calculateProbability(BigDecimal.valueOf(currentPrice), volatility, daysToExpiry, BigDecimal.valueOf(highLossStrike), BigDecimal.valueOf(highLossStrike).multiply(BigDecimal.valueOf(100)));
            final BigDecimal expectedGain = ironCondor.getMaxGain().multiply(BigDecimal.valueOf(maxWinProbability));
            final BigDecimal expectedLoss = ironCondor.getMaxLoss().multiply(BigDecimal.valueOf(maxLossProbability));
            final BigDecimal ponderedRatio = expectedGain.divide(expectedLoss, 4, RoundingMode.HALF_UP);
            System.out.println(String.format(" %d,  %.0f,  %.0f,  %.2f%%, %.2f%%,     %.2f,     %.2f, %.2f%%", daysToExpiry, ironCondor.getMaxGain(), ironCondor.getMaxLoss().negate(), maxWinProbability * 100, maxLossProbability * 100, expectedGain.doubleValue(), expectedLoss.doubleValue(), ponderedRatio.doubleValue() * 100));
        }
    }

    private static double calculateProbability(BigDecimal price, double sigma, int days, BigDecimal low, BigDecimal up) {
        double mu = -0.5 * sigma * sigma;
        double time = days / 365d;
        double denom = sigma * Math.sqrt(time);
        final double zlb = Math.log(low.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        final double zub = Math.log(up.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        return cumulativeDistribution(zub) - cumulativeDistribution(zlb);
    }

}

