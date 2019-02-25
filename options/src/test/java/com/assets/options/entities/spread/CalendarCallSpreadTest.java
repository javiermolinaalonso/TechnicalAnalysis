package com.assets.options.entities.spread;

import com.assets.options.PrintUtils;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class CalendarCallSpreadTest {

    @Test
    public void givenCalendarSpread_thenValuesAreCorrect() {
        final CalendarCallSpread spread = CalendarCallSpread.basicSpread(277, 280, 30, 60, 0.15, "SPY");

        assertThat("Max gain is correct", spread.getMaxGain(), closeTo(BigDecimal.valueOf(283), BigDecimal.valueOf(1)));
        assertThat("Max loss is correct", spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-205), BigDecimal.valueOf(1)));
        assertThat("Max loss happens far from strike", spread.getMaxLoss(), closeTo(spread.getExpirationValue(BigDecimal.valueOf(200)), BigDecimal.ONE));
        assertThat("Cost is correct", spread.getCost(), closeTo(BigDecimal.valueOf(205), BigDecimal.valueOf(1)));
        assertThat("Delta is close to zero", spread.getGreeks().getDelta(), IsCloseTo.closeTo(0d, 0.04));
        assertThat("Gamma is close to zero", spread.getGreeks().getGamma(), IsCloseTo.closeTo(0d, 0.01));
        assertThat("Theta is correct", spread.getGreeks().getTheta(), IsCloseTo.closeTo(8, 0.5));
    }

    @Test
    public void asfafafafa() {
        final CalendarCallSpread spread = CalendarCallSpread.basicSpread(277, 280, 30, 60, 0.15, "SPY");

        System.out.println(spread.getValueAt(BigDecimal.valueOf(280), LocalDate.now()));
        System.out.println(spread.getValueAt(BigDecimal.valueOf(280), LocalDate.now().plusDays(5)));
        System.out.println(spread.getValueAt(BigDecimal.valueOf(280), LocalDate.now().plusDays(30)));
    }
}