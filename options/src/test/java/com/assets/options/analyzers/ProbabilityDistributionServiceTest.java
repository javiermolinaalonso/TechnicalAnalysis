package com.assets.options.analyzers;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class ProbabilityDistributionServiceTest {

    ProbabilityDistributionService victim;

    @Test
    public void givenTenSteps_whenCalculateDistribution_expectCorrectPercentages() {
        victim = new ProbabilityDistributionService(BigDecimal.valueOf(100), 0.4, 10);

        assertThat(victim.calculate(80, 85).doubleValue(), closeTo(0.007, 0.01));
        assertThat(victim.calculate(95, 100).doubleValue(), closeTo(0.28, 0.01));
        assertThat(victim.calculate(100, 105).doubleValue(), closeTo(0.27, 0.01));
    }

}