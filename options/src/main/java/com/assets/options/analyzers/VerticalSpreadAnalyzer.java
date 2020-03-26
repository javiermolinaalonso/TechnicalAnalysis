package com.assets.options.analyzers;

import com.assets.options.entities.spread.vertical.VerticalSpread;
import org.apache.commons.lang3.Range;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class VerticalSpreadAnalyzer {

    private final Range<BigDecimal> range;
    private final LocalDate now;
    private final BigDecimal currentPrice;
    private final ProbabilityDistributionService probabilityService;
    private final Map<BigDecimal, BigDecimal> probabilities;
    private final BigDecimal step;

    public VerticalSpreadAnalyzer(Range<BigDecimal> range, LocalDate now, BigDecimal currentPrice, ProbabilityDistributionService probabilityService) {
        this.range = range;
        this.now = now;
        this.currentPrice = currentPrice;
        this.probabilityService = probabilityService;
        this.probabilities = new HashMap<>();
        this.step = getStep();
        init();
    }

    private void init() {
        BigDecimal current = range.getMinimum();

        while (current.compareTo(range.getMaximum()) < 0) {
            probabilities.put(current, probabilityService.calculate(current, current.add(step)));
            current = current.add(step);
        }
    }

    private BigDecimal getStep() {
        BigDecimal rangeDistance = range.getMaximum().subtract(range.getMinimum());
        if (rangeDistance.compareTo(BigDecimal.valueOf(100)) > 0) {
            return BigDecimal.ONE;
        }
        if (rangeDistance.compareTo(BigDecimal.valueOf(50)) > 0) {
            return BigDecimal.valueOf(0.5);
        }
        return BigDecimal.valueOf(0.25);
    }

    public SpreadAnalyzerResult analyze(VerticalSpread spread) {
        BigDecimal current = range.getMinimum();

        BigDecimal expirationValueAtPreviousPrice = null;
        BigDecimal ponderatedValue = BigDecimal.ZERO;
        BigDecimal maxExpectedLoss = spread.getMaxLoss();
        BigDecimal winProbability = BigDecimal.ZERO;
        BigDecimal averageWin = BigDecimal.ZERO;
        BigDecimal averageLoss = BigDecimal.ZERO;
        List<BigDecimal> cutPoints = new ArrayList<>();

        while (current.compareTo(range.getMaximum()) < 0) {
            BigDecimal probability = probabilities.get(current);
            if (probability.compareTo(BigDecimal.ZERO) > 0) {
                final BigDecimal expirationValueAtCurrentPrice = spread.getExpirationValue(current);
                BigDecimal ponderatedExpirationValue = expirationValueAtCurrentPrice.multiply(probability);

                boolean isExpirationValuePositive = expirationValueAtCurrentPrice.compareTo(BigDecimal.ZERO) > 0;
                if (isExpirationValuePositive) {
                    winProbability = winProbability.add(probability);
                    averageWin = averageWin.add(ponderatedExpirationValue);
                } else {
                    averageLoss = averageLoss.add(ponderatedExpirationValue.abs());
                }
                if (expirationValueAtPreviousPrice != null) {
                    boolean isPreviousExpirationValuePositive = expirationValueAtPreviousPrice.compareTo(BigDecimal.ZERO) > 0;
                    if (isExpirationValuePositive && !isPreviousExpirationValuePositive) {
                        cutPoints.add(current);
                    } else if (!isExpirationValuePositive && isPreviousExpirationValuePositive) {
                        cutPoints.add(current);
                    }
                }

                ponderatedValue = ponderatedValue.add(ponderatedExpirationValue);
                expirationValueAtPreviousPrice = expirationValueAtCurrentPrice;
            }
            current = current.add(step);
        }

        final BigDecimal tae = tae(ponderatedValue, maxExpectedLoss, probabilityService.getYearsToExpiry());
        if (averageLoss.compareTo(BigDecimal.ZERO) == 0) {
            return new SpreadAnalyzerResult(spread, BigDecimal.ONE, null, null, null, null, Collections.emptyList());
        }
        return new SpreadAnalyzerResult(spread, winProbability, averageWin.divide(averageLoss, 4, RoundingMode.HALF_UP), tae, averageWin, averageLoss, cutPoints);
    }

    private BigDecimal tae(BigDecimal expectedGain, BigDecimal maxExpectedLoss, double yearsToExpiry) {
        if (maxExpectedLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        return expectedGain.divide(maxExpectedLoss.abs(), 8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(yearsToExpiry));
    }
}
