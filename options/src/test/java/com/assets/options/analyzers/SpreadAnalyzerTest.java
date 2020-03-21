package com.assets.options.analyzers;

import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class SpreadAnalyzerTest {

    private SpreadAnalyzer victim = new SpreadAnalyzer();

    @Test
    public void testIronCondor() {
        IronCondorSpread spread = new SpreadFactory().ironCondor(
                OptionBuilder.create("SBUX", 50.67).withStrikePrice(35d).withPremium(1.89).withDaysToExpiry(30).buildPut(),
                OptionBuilder.create("SBUX", 50.67).withStrikePrice(40d).withPremium(2.75).withDaysToExpiry(30).buildPut(),
                OptionBuilder.create("SBUX", 50.67).withStrikePrice(55d).withPremium(4.10).withDaysToExpiry(30).buildCall(),
                OptionBuilder.create("SBUX", 50.67).withStrikePrice(60d).withPremium(2.48).withDaysToExpiry(30).buildCall(),
                1
        );
        SpreadAnalyzerResult analyze = victim.analyze(spread, BigDecimal.valueOf(50), LocalDate.now());

        assertThat(analyze.getWinProbability(), OptionalMatchers.isPresentAnd(closeTo(BigDecimal.valueOf(0.3485), BigDecimal.valueOf(0.001))));
        assertThat(analyze.getRiskRewardRatio(), OptionalMatchers.isPresentAnd(closeTo(BigDecimal.valueOf(0.4996), BigDecimal.valueOf(0.001))));
        assertThat(analyze.getAverageWin(), OptionalMatchers.isPresentAnd(closeTo(BigDecimal.valueOf(74.88), BigDecimal.valueOf(1))));
        assertThat(analyze.getAverageLoss(), OptionalMatchers.isPresentAnd(closeTo(BigDecimal.valueOf(149.86), BigDecimal.valueOf(1))));
        assertThat(analyze.getExpectedTae(), OptionalMatchers.isPresentAnd(closeTo(BigDecimal.valueOf(-3.563), BigDecimal.valueOf(0.01))));
        assertThat(analyze.getCutPoints(), hasItem(closeTo(BigDecimal.valueOf(37.62), BigDecimal.valueOf(0.1))));
        assertThat(analyze.getCutPoints(), hasItem(closeTo(BigDecimal.valueOf(57.51), BigDecimal.valueOf(0.1))));

    }
}