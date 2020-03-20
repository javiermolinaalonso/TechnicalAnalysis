package com.assets.options.entities;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

public class CallOptionTest {

    private LocalDate now = LocalDate.of(2016, 1, 1);
    private LocalDate expirationDate = LocalDate.of(2016, 3, 1);

    @Test
    public void testGivenPriceExactStartVolatilityExpectImpliedVolatilityMatches() {
        Option option = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withIV(0.3).withDaysToExpiry(60).buildCall();
        Option priceOption = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withPremium(0.4849).withDaysToExpiry(60).buildCall();

        assertEquals(option.getPremium().doubleValue(), priceOption.getPremium().doubleValue(), 0.001d);
        assertEquals(option.getImpliedVolatility(), option.getImpliedVolatility(), 0.001d);
    }

    @Test
    public void testGivenPrice20VolatilityExpectImpliedVolatilityMatches() throws Exception {
        Option option = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withIV(0.2).withDaysToExpiry(60).buildCall();
        Option priceOption = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withPremium(0.3234).withDaysToExpiry(60).buildCall();

        assertEquals(option.getPremium().doubleValue(), priceOption.getPremium().doubleValue(), 0.001d);
        assertEquals(option.getImpliedVolatility(), option.getImpliedVolatility(), 0.001d);
    }

    @Test
    public void testGivenTwoOptionsWithDifferentPremiumExpectHigherVolatilityAtHigherPremium() throws Exception {
        Option lowerVolatilityOption = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withPremium(0.5).withDaysToExpiry(60).buildCall();
        Option higherVolatilityOption = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withPremium(0.7).withDaysToExpiry(60).buildCall();

        assertTrue(lowerVolatilityOption.getImpliedVolatility().compareTo(higherVolatilityOption.getImpliedVolatility()) < 0);
    }

    @Test
    public void givenCallOption_assertThetaIsNegative() {
        Option option = OptionBuilder.create("IWM", 10d).withStrikePrice(10d).withPremium(0.5).withDaysToExpiry(60).buildCall();

        assertThat(option.getGreeks().getTheta(), lessThan(0d));
    }
}