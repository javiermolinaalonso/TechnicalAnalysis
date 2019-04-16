package com.assets.options.entities.spread;

import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class VerticallPutSpreadTest {


    @Test
    public void givenVerticalPutCreditSpread_whenGetMaxProfit_expectCorrect() {
        final VerticalPutSpread spread = VerticalPutSpread.basicPutSpread(
                277,
                275,
                280,
                60,
                0.22,
                "PHOB");

        assertThat(spread.getMaxGain(), closeTo(BigDecimal.valueOf(260), BigDecimal.valueOf(1)));
        assertThat(spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-238), BigDecimal.valueOf(1)));
        assertThat(spread.getCost(), closeTo(BigDecimal.valueOf(-260), BigDecimal.valueOf(1)));

        assertThat("Max loss happens when price is low", spread.getExpirationValue(BigDecimal.valueOf(100)), closeTo(spread.getMaxLoss(), BigDecimal.valueOf(3)));
        assertThat("Max gain happens when price is high", spread.getExpirationValue(BigDecimal.valueOf(500)), closeTo(spread.getMaxGain(), BigDecimal.valueOf(3)));

    }

    @Test
    public void givenVerticalPutDebitSpread_whenGetMaxProfit_expectCorrect() {
        final VerticalPutSpread spread = VerticalPutSpread.basicPutSpread(
                277,
                280,
                275,
                60,
                0.22,
                "PHOB");

        assertThat("Max gain is correct", spread.getMaxGain(), closeTo(BigDecimal.valueOf(235), BigDecimal.valueOf(1)));
        assertThat("Max loss is correct", spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-264), BigDecimal.valueOf(1)));
        assertThat("Cost is correct", spread.getCost(), closeTo(BigDecimal.valueOf(264), BigDecimal.valueOf(1)));
        assertThat("Max loss happens when price is high", spread.getExpirationValue(BigDecimal.valueOf(500)), closeTo(spread.getMaxLoss(), BigDecimal.valueOf(3)));
        assertThat("Max gain happens when price is low", spread.getExpirationValue(BigDecimal.valueOf(100)), closeTo(spread.getMaxGain(), BigDecimal.valueOf(3)));


        assertThat("Delta is correct", spread.getGreeks().getDelta(), IsCloseTo.closeTo(-0.08, 0.01));
        assertThat("Gamma is correct", spread.getGreeks().getGamma(), IsCloseTo.closeTo(0.00008, 0.00001));
        assertThat("Vega is correct", spread.getGreeks().getVega(), IsCloseTo.closeTo(0.24, 0.1));
        assertThat("Theta is correct", spread.getGreeks().getTheta(), IsCloseTo.closeTo(-0.0003, 0.0001));
    }
}
