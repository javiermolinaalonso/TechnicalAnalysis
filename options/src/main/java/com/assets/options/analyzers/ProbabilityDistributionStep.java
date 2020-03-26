package com.assets.options.analyzers;

import java.math.BigDecimal;

public class ProbabilityDistributionStep {

    private final BigDecimal start;
    private final BigDecimal end;
    private final BigDecimal probability;

    public ProbabilityDistributionStep(BigDecimal start, BigDecimal end, BigDecimal probability) {
        this.start = start;
        this.end = end;
        this.probability = probability;
    }

    public BigDecimal getStart() {
        return start;
    }

    public BigDecimal getEnd() {
        return end;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return String.format("[%.2f,%.2f] %.4f%%", start.doubleValue(), end.doubleValue(), probability.doubleValue());
    }
}
