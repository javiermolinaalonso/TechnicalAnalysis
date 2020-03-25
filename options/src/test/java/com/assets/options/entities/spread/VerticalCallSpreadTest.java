package com.assets.options.entities.spread;

import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.vertical.VerticalSpread;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

public class VerticalCallSpreadTest {

    @Test
    public void givenVerticalDebitSpread_whenGetMaxProfit_expectCorrect() {

        VerticalSpread spread = new SpreadFactory().bullCallSpread(
                OptionBuilder.create("PHOB", 277).withDaysToExpiry(60).withStrikePrice(280).withIV(0.22).buildCall(),
                OptionBuilder.create("PHOB", 277).withDaysToExpiry(60).withStrikePrice(285).withIV(0.22).buildCall(),
                1
        );

        assertThat(spread.getMaxGain(), closeTo(BigDecimal.valueOf(300), BigDecimal.valueOf(1)));
        assertThat(spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-200), BigDecimal.valueOf(1)));
        assertThat(spread.getCost(), closeTo(BigDecimal.valueOf(200), BigDecimal.valueOf(1)));

        assertThat(spread.getGreeks().getDelta(), greaterThan(0d));
        assertThat(spread.getGreeks().getGamma(), greaterThan(0d));
        assertThat(spread.getGreeks().getTheta(), lessThan(0d));
    }

    @Test
    public void givenVerticalCreditSpread_whenGetMaxProfit_expectCorrect() {
        VerticalSpread spread = new SpreadFactory().bearCallSpread(
                OptionBuilder.create("PHOB", 277).withDaysToExpiry(60).withStrikePrice(275).withIV(0.22).buildCall(),
                OptionBuilder.create("PHOB", 277).withDaysToExpiry(60).withStrikePrice(280).withIV(0.22).buildCall(),
                1
        );

        assertThat("Max gain is correct", spread.getMaxGain(), closeTo(BigDecimal.valueOf(235), BigDecimal.valueOf(1)));
        assertThat("Max loss is correct", spread.getMaxLoss(), closeTo(BigDecimal.valueOf(-264), BigDecimal.valueOf(1)));
        assertThat("Cost is correct", spread.getCost(), closeTo(BigDecimal.valueOf(-235), BigDecimal.valueOf(1)));
        assertThat("Delta is correct", spread.getGreeks().getDelta(), IsCloseTo.closeTo(-0.08, 0.01));
        assertThat("Gamma is correct", spread.getGreeks().getGamma(), IsCloseTo.closeTo(0.00008, 0.00001));
        assertThat("Vega is correct", spread.getGreeks().getVega(), IsCloseTo.closeTo(0.24, 0.1));
        assertThat("Theta is correct", spread.getGreeks().getTheta(), IsCloseTo.closeTo(-0.0003, 0.0001));
        assertThat("Volatility is the average", spread.getVolatility(), IsCloseTo.closeTo(0.22, 0.001));
    }

    @Test
    public void givenVerticalSpread_whenGetMaxProfit_expectNegative() {
        VerticalSpread spread = new SpreadFactory().bullCallSpread(
                OptionBuilder.create("INTC", 32.89)
                        .withDaysToExpiry(17)
                        .withBidAsk(22.7,22.9)
                        .withStrikePrice(10)
                        .buildCall(),
                OptionBuilder.create("INTC", 32.89)
                        .withDaysToExpiry(17)
                        .withBidAsk(17.7,17.9)
                        .withStrikePrice(15)
                        .buildCall(),
                1
        );
        assertThat("Max gain is negative", spread.getMaxGain(), lessThan(BigDecimal.ZERO));
    }
}