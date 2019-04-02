package com.assets.options.entities.spread;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IronCondorSpreadTest {

    @Test
    public void givenIronCondorSpread_whenGetMaxProfit_expectCorrect() {
        final IronCondorSpread spy = IronCondorSpread.basicIronCondor(278.8, 273, 275, 285, 287,
                21, 0.11, "SPY");

        assertThat(spy.getCost(), is(spy.getMaxGain().negate()));
        assertThat(spy.getMaxGain(), is(spy.getExpirationValue(BigDecimal.valueOf(280))));
        assertThat(spy.getMaxLoss(), is(spy.getExpirationValue(BigDecimal.valueOf(270))));
        assertThat(spy.getGreeks().getTheta(), greaterThan(0d));
        assertThat(spy.getGreeks().getDelta(), closeTo(0d, 0.01d));
    }
}
