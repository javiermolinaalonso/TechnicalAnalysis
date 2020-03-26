package com.assets.options.analyzers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static net.finmath.functions.NormalDistribution.cumulativeDistribution;

public class ProbabilityDistributionService {

    private final BigDecimal price;
    private final double mu;
    private final double time;
    private final double denom;

    public ProbabilityDistributionService(BigDecimal price, double iv, int days) {
        this.mu = -0.5 * iv * iv;
        this.price = price;
        this.time = days / 365d;
        this.denom = iv * Math.sqrt(time);
    }

    public BigDecimal calculate(double low, double up) {
            return calculate(BigDecimal.valueOf(low), BigDecimal.valueOf(up));
    }

    public BigDecimal calculate(BigDecimal low, BigDecimal up) {
        final double zlb = Math.log(low.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        final double zub = Math.log(up.divide(price, 8, RoundingMode.HALF_UP).doubleValue() - mu * time) / denom;
        return BigDecimal.valueOf(cumulativeDistribution(zub) - cumulativeDistribution(zlb));
    }

    public double getYearsToExpiry() {
        return time;
    }
}
