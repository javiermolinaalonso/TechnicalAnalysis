package com.assets.options.analyzers;

import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import com.assets.options.entities.spread.vertical.VerticalSpread;
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

    @Test
    public void test() {
//        OptionTrade{option=[SPY, P, 239.32, 2020-03-24] at 255.00, 2020-03-26, [17.64, 17.94], Greeks: [-0.7940, 0.0161, -1.2616, 5.0485, -1.1379], premium=17.94, contracts=1, ticker='SPY', cost=1795.00, mini=false, tradeComission=1.0},
//        OptionTrade{option=[SPY, P, 239.32, 2020-03-24] at 360.00, 2020-03-26, [122.96, 124.56], Greeks: [-1.0000, 0.0000, 0.0010, 0.0000, -1.9726], premium=122.96, contracts=-1, ticker='SPY', cost=-12295.00, mini=false, tradeComission=1.0}]},

    VerticalSpread spread = new SpreadFactory().bullPutSpread(
                OptionBuilder.create("SPY", 239.32).withStrikePrice(255).withBidAsk(15.43, 15.78).withDaysToExpiry(2).buildPut(),
                OptionBuilder.create("SPY", 239.32).withStrikePrice(360).withBidAsk(119.55, 120.20).withDaysToExpiry(2).buildPut(),
                1
        );

        SpreadAnalyzerResult analyze = victim.analyze(spread, BigDecimal.valueOf(239.32), LocalDate.now());

        System.out.println(analyze);
    }
}