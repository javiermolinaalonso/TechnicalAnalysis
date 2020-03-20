package com.assets.options.blackscholes;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class IVolatilityTest {

    @Test
    public void volatilityIsProperlyCalculated_whenGivenOptionPrice() {
        Option callV = OptionBuilder.create("IWM", 143.45).withStrikePrice(144d).withPremium(3.7).withDaysToExpiry(44).withRiskFree(0.015).buildCall();
        Option putV = OptionBuilder.create("IWM", 143.45).withStrikePrice(144d).withPremium(4.16).withDaysToExpiry(44).withRiskFree(0.015).buildPut();

        assertThat(callV.getImpliedVolatility(), is(closeTo(0.1933, 0.011)));
        assertThat(putV.getImpliedVolatility(), is(closeTo(0.1926, 0.01)));
    }

    @Test
    public void volatilityIsProperlyCalculated_whenGivenOptionPriceDuo() {
        Option callV = OptionBuilder.create("IWM", 143.45).withStrikePrice(165d).withPremium(1.57).withDaysToExpiry(247).withRiskFree(0.015).buildCall();
        assertThat(callV.getImpliedVolatility(), is(closeTo(0.1569, 0.011)));
    }
}
