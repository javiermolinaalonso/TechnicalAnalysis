package com.assets.options.analyzers;

import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.vertical.VerticalSpread;
import org.apache.commons.lang3.Range;
import org.hamcrest.number.BigDecimalCloseTo;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class SpreadAnalyzerTest {


//    @Test
//    public void testIronCondor() {
//        IronCondorSpread spread = new SpreadFactory().ironCondor(
//                OptionBuilder.create("SBUX", 50.67).withStrikePrice(35d).withPremium(1.89).withDaysToExpiry(30).buildPut(),
//                OptionBuilder.create("SBUX", 50.67).withStrikePrice(40d).withPremium(2.75).withDaysToExpiry(30).buildPut(),
//                OptionBuilder.create("SBUX", 50.67).withStrikePrice(55d).withPremium(4.10).withDaysToExpiry(30).buildCall(),
//                OptionBuilder.create("SBUX", 50.67).withStrikePrice(60d).withPremium(2.48).withDaysToExpiry(30).buildCall(),
//                1
//        );
//        SpreadAnalyzerResult analyze = victim.analyze(spread, valueOf(50), LocalDate.now());
//
//        assertThat(analyze.getWinProbability(), isPresentAnd(closeTo(valueOf(0.3485), valueOf(0.001))));
//        assertThat(analyze.getRiskRewardRatio(), isPresentAnd(closeTo(valueOf(0.4996), valueOf(0.001))));
//        assertThat(analyze.getAverageWin(), isPresentAnd(closeTo(valueOf(74.88), valueOf(1))));
//        assertThat(analyze.getAverageLoss(), isPresentAnd(closeTo(valueOf(149.86), valueOf(1))));
//        assertThat(analyze.getExpectedTae(), isPresentAnd(closeTo(valueOf(-3.563), valueOf(0.01))));
//        assertThat(analyze.getCutPoints(), hasItem(closeTo(valueOf(37.62), valueOf(0.1))));
//        assertThat(analyze.getCutPoints(), hasItem(closeTo(valueOf(57.51), valueOf(0.1))));
//
//    }

    @Test
    public void test() {
//        OptionTrade{option=[SPY, P, 239.32, 2020-03-24] at 255.00, 2020-03-26, [17.64, 17.94], Greeks: [-0.7940, 0.0161, -1.2616, 5.0485, -1.1379], premium=17.94, contracts=1, ticker='SPY', cost=1795.00, mini=false, tradeComission=1.0},
//        OptionTrade{option=[SPY, P, 239.32, 2020-03-24] at 360.00, 2020-03-26, [122.96, 124.56], Greeks: [-1.0000, 0.0000, 0.0010, 0.0000, -1.9726], premium=122.96, contracts=-1, ticker='SPY', cost=-12295.00, mini=false, tradeComission=1.0}]},

        ProbabilityDistributionService probabilityService = new ProbabilityDistributionService(valueOf(246), 0.73, 23);
        VerticalSpreadAnalyzer victim = new VerticalSpreadAnalyzer(
                Range.between(valueOf(150), valueOf(350)),
                LocalDate.now(Clock.systemUTC()),
                valueOf(246),
                probabilityService
        );
        VerticalSpread spread = new SpreadFactory().bullCallSpread(
                OptionBuilder.create("SPY", 246).withStrikePrice(210).withBidAsk(46.78, 47.25).withDaysToExpiry(23).withIV(0.74).buildCall(),
                OptionBuilder.create("SPY", 246).withStrikePrice(220).withBidAsk(39.79, 40.49).withDaysToExpiry(23).withIV(0.71).buildCall(),
                1
        );

        SpreadAnalyzerResult analyze = victim.analyze(spread);

        assertThat(analyze.getAverageWin(), isPresentAnd(BigDecimalCloseTo.closeTo(valueOf(170), valueOf(1))));
        assertThat(analyze.getAverageLoss(), isPresentAnd(BigDecimalCloseTo.closeTo(valueOf(189), valueOf(1))));
        assertThat(analyze.getWinProbability(), isPresentAnd(BigDecimalCloseTo.closeTo(valueOf(0.68), valueOf(0.01))));
        assertThat(analyze.getRiskRewardRatio(), isPresentAnd(BigDecimalCloseTo.closeTo(valueOf(0.9), valueOf(0.1))));
        assertThat(analyze.getCutPoints(), contains(BigDecimalCloseTo.closeTo(valueOf(218), valueOf(1))));
    }

    @Test
    public void name() {
//        [spread=BaseOptionSpread{options=OptionPortfolio{trades=[
//        OptionTrade{option=[SPY, P, 246.83, 2020-03-25] at 299.00, 2020-04-17, [52.68, 53.31], Greeks: [-0.9184, 0.0047, -0.1055, 9.3486, -17.6430], premium=52.68, contracts=-1, ticker='SPY', cost=-5267.00, mini=false, tradeComission=1.0},
//        OptionTrade{option=[SPY, P, 246.83, 2020-03-25] at 375.00, 2020-04-17, [52.33, 52.73], Greeks: [-1.0000, 0.0000, 0.0010, 0.0000, -23.6286], premium=52.73, contracts=1, ticker='SPY', cost=5274.00, mini=false, tradeComission=1.0}]}, mini=false},
//        winProbability=99.68%, riskRewardRatio=335372.5291, averageWin=7369.02, averageLoss=0.02, cutPoints=[375.42], expectedTae=6633.54%]
        ProbabilityDistributionService probabilityService = new ProbabilityDistributionService(valueOf(246), 0.73, 23);
        VerticalSpreadAnalyzer victim = new VerticalSpreadAnalyzer(
                Range.between(valueOf(150), valueOf(350)),
                LocalDate.now(Clock.systemUTC()),
                valueOf(246),
                probabilityService
        );
        VerticalSpread spread = new SpreadFactory().verticalSpread(
                OptionBuilder.create("SPY", 246).withStrikePrice(299).withBidAsk(52.68, 53.31).withDaysToExpiry(23).withIV(0.74).buildPut(),
                OptionBuilder.create("SPY", 246).withStrikePrice(375).withBidAsk(52.33, 52.73).withDaysToExpiry(23).withIV(0.71).buildPut(),
                1
        );

        SpreadAnalyzerResult analyze = victim.analyze(spread);
    }
}