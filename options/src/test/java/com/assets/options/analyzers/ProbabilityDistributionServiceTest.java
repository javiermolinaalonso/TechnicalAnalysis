package com.assets.options.analyzers;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class ProbabilityDistributionServiceTest {

    ProbabilityDistributionService victim;

    @Test
    public void givenTenSteps_whenCalculateDistribution_expectCorrectPercentages() {
        victim = new ProbabilityDistributionService(100, 0.4, 10);

        for (int i = 0; i < 200; i+=5) {
            System.out.println(victim.calculate(i, i+5));
        }
        assertThat(victim.calculate(80, 85), closeTo(0.04, 0.01));
        assertThat(victim.calculate(95, 100), closeTo(0.19, 0.01));
        assertThat(victim.calculate(100, 105), closeTo(0.19, 0.01));
    }

    @Test
    public void givenTenSteps_whenCalculateDistribution_expectCorrectPercentages2() {
        victim = new ProbabilityDistributionService(246, 0.7, 50);

        System.out.println(victim.calculate(-700, 220));
        System.out.println(victim.calculate(0, 220));
    }
}