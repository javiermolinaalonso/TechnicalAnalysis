package com.assets.options.analyzers;

import com.assets.options.entities.spread.OptionSpread;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class SpreadAnalyzerResult {

    private final OptionSpread spread;
    private final BigDecimal winProbability;

    /**
     * This computes the ratio of the AVERAGE win vs the AVERAGE loss
     */
    private final BigDecimal riskRewardRatio;

    private final BigDecimal expectedTae;

    private final BigDecimal averageWin;
    private final BigDecimal averageLoss;
    private final List<BigDecimal> cutPoints;

    public SpreadAnalyzerResult(OptionSpread spread, BigDecimal winProbability, BigDecimal riskRewardRatio, BigDecimal expectedTae, BigDecimal averageWin, BigDecimal averageLoss, List<BigDecimal> cutPoints) {
        this.spread = spread;
        this.winProbability = winProbability;
        this.riskRewardRatio = riskRewardRatio;
        this.expectedTae = expectedTae;
        this.averageWin = averageWin;
        this.averageLoss = averageLoss;
        this.cutPoints = cutPoints;
    }

    public OptionSpread getSpread() {
        return spread;
    }

    public Optional<BigDecimal> getWinProbability() {
        return Optional.ofNullable(winProbability);
    }

    public Optional<BigDecimal> getRiskRewardRatio() {
        return Optional.ofNullable(riskRewardRatio);
    }

    public Optional<BigDecimal> getExpectedTae() {
        return Optional.ofNullable(expectedTae);
    }

    public List<BigDecimal> getCutPoints() {
        return cutPoints;
    }

    public Optional<BigDecimal> getAverageWin() {
        return Optional.ofNullable(averageWin);
    }

    public Optional<BigDecimal> getAverageLoss() {
        return Optional.ofNullable(averageLoss);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("spread=" + spread.toString())
                .add("winProbability=" + getWinProbability().map(winProbability -> winProbability.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP) + "%").orElse("?"))
                .add("riskRewardRatio=" + getRiskRewardRatio().map(BigDecimal::toString).orElse("?"))
                .add("averageWin=" + getAverageWin().map(averageWin -> averageWin.setScale(2, RoundingMode.HALF_UP).toString()).orElse("?"))
                .add("averageLoss=" + getAverageLoss().map(averageLoss -> averageLoss.setScale(2, RoundingMode.HALF_UP).toString()).orElse("?"))
                .add("cutPoints=" + cutPoints)
                .add("expectedTae=" + getExpectedTae().map(expectedTae -> expectedTae.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString()+ "%").orElse("?"))
                .toString();
    }
}
