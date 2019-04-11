package com.assets.options.analyzers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.StringJoiner;

public class SpreadAnalyzerResult {

    private final BigDecimal winProbability;

    /**
     * This computes the ratio of the AVERAGE win vs the AVERAGE loss
     */
    private final BigDecimal riskRewardRatio;

    private final BigDecimal expectedTae;

    private final BigDecimal averageWin;
    private final BigDecimal averageLoss;
    private final List<BigDecimal> cutPoints;

    public SpreadAnalyzerResult(BigDecimal winProbability, BigDecimal riskRewardRatio, BigDecimal expectedTae, BigDecimal averageWin, BigDecimal averageLoss, List<BigDecimal> cutPoints) {
        this.winProbability = winProbability;
        this.riskRewardRatio = riskRewardRatio;
        this.expectedTae = expectedTae;
        this.averageWin = averageWin;
        this.averageLoss = averageLoss;
        this.cutPoints = cutPoints;
    }

    public BigDecimal getWinProbability() {
        return winProbability;
    }

    public BigDecimal getRiskRewardRatio() {
        return riskRewardRatio;
    }

    public BigDecimal getExpectedTae() {
        return expectedTae;
    }

    public List<BigDecimal> getCutPoints() {
        return cutPoints;
    }

    public BigDecimal getAverageWin() {
        return averageWin;
    }

    public BigDecimal getAverageLoss() {
        return averageLoss;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("winProbability=" + winProbability.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP) + "%")
                .add("riskRewardRatio=" + riskRewardRatio)
                .add("averageWin=" + averageWin.setScale(2, RoundingMode.HALF_UP))
                .add("averageLoss=" + averageLoss.setScale(2, RoundingMode.HALF_UP))
                .add("cutPoints=" + cutPoints)
                .add("expectedTae=" + expectedTae.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)+ "%")
                .toString();
    }
}
