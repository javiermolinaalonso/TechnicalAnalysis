package com.assets.options.entities;

import com.assets.options.entities.spread.OptionTradeFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OptionTradeTest {

    private static final double MIN = 9;
    private static final double MAX = 12.5d;
    private static final double STRIKE_PRICE = 11;

    private LocalDate firstTrade;
    private LocalDate expirationDate;

    private double currentPrice = 10d;
    private double callPrice = 0.5;
    private double putPrice = 1.2;

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.US);
        this.firstTrade = LocalDate.of(2016, 1, 5);
        this.expirationDate = LocalDate.of(2016, 3, 15);
    }

    @Test
    public void testGivenLongCallWhenExpirationValueExpectThreeCorrectPoints() {
        Option option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(callPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildCall();
        OptionTrade optionTrade = OptionTradeFactory.buy(option, 5, 2);

        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(490).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testGivenLongCallWhenValueAtSpecificDateExpectThreeCorrectPoints() {
        Option option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(callPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildCall();
        OptionTrade optionTrade = OptionTradeFactory.buy(option, 5, 2);
        assertEquals(optionTrade.getExpectedValue(BigDecimal.valueOf(STRIKE_PRICE), expirationDate), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)));
    }

    @Test
    public void testGivenShortCallExpectExpirationValue() {
        CallOption option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(callPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildCall();

        OptionTrade optionTrade = OptionTradeFactory.write(option, 10, 2);

        assertEquals(BigDecimal.valueOf(480).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(480).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-1020).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testGivenLongPutExpectExpirationValue() {
        PutOption option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(putPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildPut();
        OptionTrade optionTrade = OptionTradeFactory.buy(option, 7, 2);

        assertEquals(BigDecimal.valueOf(546).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-854).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-854).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testShortPutExpectExpirationValue() {
        PutOption option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(putPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildPut();
        OptionTrade optionTrade = OptionTradeFactory.write(option, 3, 2);

        assertEquals(BigDecimal.valueOf(-246).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(354).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(354).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void givenShortPut_whenGetGreeks_expectCorrectSign() {
        PutOption option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(putPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildPut();
        OptionTrade optionTrade = OptionTradeFactory.write(option, 1, 2);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), greaterThan(0d));
        assertThat(greeks.getTheta(), greaterThan(0d));
        assertThat(greeks.getGamma(), lessThan(0d));
        assertThat(greeks.getVega(), lessThan(0d));
    }

    @Test
    public void givenLongPut_whenGetGreeks_expectCorrectSign() {
        PutOption option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(putPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildPut();
        OptionTrade optionTrade = OptionTradeFactory.buy(option, 1, 2);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), lessThan(0d));
        assertThat(greeks.getTheta(), lessThan(0d));
        assertThat(greeks.getGamma(), greaterThan(0d));
        assertThat(greeks.getVega(), greaterThan(0d));
    }

    @Test
    public void givenLongCall_whenGetGreeks_expectCorrectSign() {
        Option option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(callPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildCall();
        OptionTrade optionTrade = OptionTradeFactory.buy(option, 1, 2);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), greaterThan(0d));
        assertThat(greeks.getTheta(), lessThan(0d));
        assertThat(greeks.getGamma(), greaterThan(0d));
        assertThat(greeks.getVega(), greaterThan(0d));
    }

    @Test
    public void givenShortCall_whenGetGreeks_expectCorrectSign() {
        Option option = OptionBuilder.create("TICK", currentPrice).withStrikePrice(STRIKE_PRICE).withPremium(callPrice).withCurrentDate(firstTrade).withExpirationAt(expirationDate).withRiskFree(0d).buildCall();
        OptionTrade optionTrade = OptionTradeFactory.write(option, 1, 2);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), lessThan(0d));
        assertThat(greeks.getTheta(), greaterThan(0d));
        assertThat(greeks.getGamma(), lessThan(0d));
        assertThat(greeks.getVega(), lessThan(0d));
    }
}
