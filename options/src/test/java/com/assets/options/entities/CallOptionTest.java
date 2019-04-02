package com.assets.options.entities;

import com.assets.options.PrintUtils;
import com.assets.options.entities.spread.OptionSpread;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CallOptionTest {

    private LocalDate now = LocalDate.of(2016, 1, 1);
    private LocalDate expirationDate = LocalDate.of(2016, 3, 1);

    @Test
    public void testGivenPriceExactStartVolatilityExpectImpliedVolatilityMatches() throws Exception {

        Option option = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, now, expirationDate, 0.3d, 0d);

        Option priceOption = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(0.4849), now, expirationDate, 0d);

        assertEquals(option.getPremium().doubleValue(), priceOption.getPremium().doubleValue(), 0.001d);
        assertEquals(option.getVolatility(), option.getVolatility(), 0.001d);
    }

    @Test
    public void testGivenPrice20VolatilityExpectImpliedVolatilityMatches() throws Exception {
        Option option = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, now, expirationDate, 0.2d, 0d);
        Option priceOption = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(0.3234), now, expirationDate, 0d);

        assertEquals(option.getPremium().doubleValue(), priceOption.getPremium().doubleValue(), 0.001d);
        assertEquals(option.getVolatility(), option.getVolatility(), 0.001d);
    }

    @Test
    public void testGivenTwoOptionsWithDifferentPremiumExpectHigherVolatilityAtHigherPremium() throws Exception {
        Option lowerVolatilityOption = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(0.5), now, expirationDate, 0d);
        Option higherVolatilityOption = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(0.7), now, expirationDate, 0d);

        assertTrue(lowerVolatilityOption.getPremium().compareTo(higherVolatilityOption.getPremium()) < 0);
    }

    @Test
    public void givenCallOption_assertThetaIsNegative() {
        final CallOption ko = new CallOption(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(0.5), now, expirationDate, 0d);

        assertThat(ko.getGreeks().getTheta(), lessThan(0d));
    }
}