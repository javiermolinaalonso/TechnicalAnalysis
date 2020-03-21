package com.assets.options.entities.spread;

import com.assets.options.entities.OptionBuilder;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class CalendarCallSpreadTest {

    @Test
    public void givenCalendarSpread_thenValuesAreCorrect() {
        CalendarCallSpread spread = new SpreadFactory().calendarCallSpread(
                OptionBuilder.create("SPY", 277).withDaysToExpiry(30).withStrikePrice(280).withIV(0.15).withRiskFree(0.01d).buildCall(),
                OptionBuilder.create("SPY", 277).withDaysToExpiry(60).withStrikePrice(280).withIV(0.15).withRiskFree(0.01d).buildCall(),
                1
        );

        assertThat("Max gain is correct", spread.getMaxGain(), closeTo(BigDecimal.valueOf(274), BigDecimal.valueOf(1)));
        assertThat("Max loss is correct", spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-205), BigDecimal.valueOf(1)));
        assertThat("Max loss happens far from strike", spread.getMaxLoss(), closeTo(spread.getExpirationValue(BigDecimal.valueOf(200)), BigDecimal.ONE));
        assertThat("Cost is correct", spread.getCost(), closeTo(BigDecimal.valueOf(205), BigDecimal.valueOf(1)));

        assertThat("Delta is close to zero", spread.getGreeks().getDelta(), IsCloseTo.closeTo(0d, 0.04));
        assertThat("Gamma is close to zero", spread.getGreeks().getGamma(), IsCloseTo.closeTo(0d, 0.01));
        assertThat("Theta is correct", spread.getGreeks().getTheta(), IsCloseTo.closeTo(0.021, 0.001));
    }

}