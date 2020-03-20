package com.assets.options.entities.spread;

import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class IronCondorSpreadTest {

    @Test
    public void givenIronCondorSpread_whenGetMaxProfit_expectCorrect() {
        IronCondorSpread spy = new SpreadFactory().ironCondor(
                OptionBuilder.create("SPY", 306.05).withStrikePrice(290).withIV(0.1957).withDaysToExpiry(25).buildPut(),
                OptionBuilder.create("SPY", 306.05).withStrikePrice(303).withIV(0.1957).withDaysToExpiry(25).buildPut(),
                OptionBuilder.create("SPY", 306.05).withStrikePrice(309).withIV(0.1957).withDaysToExpiry(25).buildCall(),
                OptionBuilder.create("SPY", 306.05).withStrikePrice(313).withIV(0.1957).withDaysToExpiry(25).buildCall(),
                1
        );

        assertThat(spy.getCost(), is(spy.getMaxGain().negate()));
        assertThat(spy.getMaxGain(), is(spy.getExpirationValue(BigDecimal.valueOf(305))));
        assertThat(spy.getMaxLoss(), is(spy.getExpirationValue(BigDecimal.valueOf(270))));
        assertThat(spy.getGreeks().getTheta(), greaterThan(0d));
    }

}
