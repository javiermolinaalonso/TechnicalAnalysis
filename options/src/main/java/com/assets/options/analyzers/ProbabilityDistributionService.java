package com.assets.options.analyzers;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ProbabilityDistributionService {

    private static final double GOLDEN_RATIO = 1.6179775280898876;
    private final NormalDistribution distribution;

    public ProbabilityDistributionService(double price, double iv, int days) {
        final double stddev = Math.pow(Math.log(Math.pow(days, GOLDEN_RATIO) + 1), Math.pow(1 + iv, GOLDEN_RATIO));
        this.distribution = new NormalDistribution(price, stddev);
    }

    public double calculate(double low, double high) {
        return distribution.probability(low, high);
    }

}
