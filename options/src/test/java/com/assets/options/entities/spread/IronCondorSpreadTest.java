package com.assets.options.entities.spread;

import com.assets.options.PrintUtils;
import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.analyzers.SpreadAnalyzerResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IronCondorSpreadTest {

    @Test
    public void givenIronCondorSpread_whenGetMaxProfit_expectCorrect() {
        final IronCondorSpread spy = IronCondorSpread.basicIronCondor(306.05,
                290, 303, 309, 313,
                25, 0.1957, "SPY");

        assertThat(spy.getCost(), is(spy.getMaxGain().negate()));
        assertThat(spy.getMaxGain(), is(spy.getExpirationValue(BigDecimal.valueOf(305))));
        assertThat(spy.getMaxLoss(), is(spy.getExpirationValue(BigDecimal.valueOf(270))));
        assertThat(spy.getGreeks().getTheta(), greaterThan(0d));
//        assertThat(spy.getGreeks().getDelta(), closeTo(0d, 0.02d));
    }



//    Expectation: [winProbability=68.58%, riskRewardRatio=1.1580, averageWin=196.43, averageLoss=169.63, cutPoints=[324.1006], expectedTae=21.02%]
//    Purchased: {[300.00/304.00/319.00/330.00] @ 1990-10-31, vol: 0.1173, [1.00/1.65/6.04/2.30], Max Loss:-664.56, Max Win:435.44} purchased at 1990-08-22 with current price 316.55
//    Result=122.53, currentStockPrice=307.06, date=1990-08-23
//            122.53 in 1 days


    @Test
    public void weirdScenarios() {
        final IronCondorSpread spread = IronCondorSpread.basicIronCondor(316.55, 300, 304, 319, 330, 68, 0.1173, "wut");
        final IronCondorSpread spread2 = IronCondorSpread.basicIronCondor(307.06, 300, 304, 319, 330, 67, 0.1173, "wut");
        final SpreadAnalyzerResult analyze = new SpreadAnalyzer().analyze(spread, BigDecimal.valueOf(316.55), LocalDate.now());

        System.out.println(spread.getCost());
        System.out.println(spread2.getCost());
    }
}
