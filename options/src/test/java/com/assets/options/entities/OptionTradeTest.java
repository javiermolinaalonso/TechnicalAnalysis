package com.assets.options.entities;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import static com.assets.options.PrintUtils.print;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OptionTradeTest {

    private static final double MIN = 9;
    private static final double MAX = 12.5d;
    private static final int STRIKE_PRICE = 11;

    private LocalDate firstTrade;
    private LocalDate expirationDate;

    private double volatility = 0.2d;
    private BigDecimal currentPrice = BigDecimal.valueOf(10);
    private BigDecimal callPrice = BigDecimal.valueOf(0.5);
    private BigDecimal putPrice = BigDecimal.valueOf(1.2);

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.US);
        this.firstTrade = LocalDate.of(2016, 1, 5);
        this.expirationDate = LocalDate.of(2016, 3, 15);
    }

    @Test
    public void testGivenSameOptionsDifferentEndDateExpectCorrectPrice() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option firstOption = new PutOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade foo = new OptionTrade(firstOption, 10, "FOO", BigDecimal.ZERO, false);

        LocalDate date = LocalDate.of(firstTrade.getYear(), firstTrade.getMonth(), firstTrade.getDayOfMonth());

        while (date.isBefore(expirationDate)) {
            date = date.plusDays(1);
            Option option = foo.getExpectedOption(currentPrice, date, volatility);
            System.out.println(String.format("%.5f, %s", option.getPremium(), option.getGreeks()));
        }
    }

    @Test
    public void testGivenLongCallWhenExpirationValueExpectThreeCorrectPoints() throws Exception {
        Option option = new CallOption(null, currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 5, "FOO", BigDecimal.valueOf(2), false);

        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(490).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testGivenLongCallWhenValueAtSpecificDateExpectThreeCorrectPoints() throws Exception {
        Option option = new CallOption(null, currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 5, "FOO", BigDecimal.valueOf(2), false);
        assertEquals(optionTrade.getExpectedValue(BigDecimal.valueOf(STRIKE_PRICE), expirationDate), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)));
    }

    @Test
    public void testGivenLongCallWhenValueAtSpecificDateExpectThreeCorrectPoints2() throws Exception {
        Option option = new CallOption(null, currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 5, "FOO", BigDecimal.valueOf(2), false);
    }

    @Test
    public void testGivenShortCallExpectExpirationValue() throws Exception {
        Option option = new CallOption(null, currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, -10, "FOO", BigDecimal.valueOf(2), false);

        assertEquals(BigDecimal.valueOf(480).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(480).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-1020).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testGivenLongPutExpectExpirationValue() throws Exception {
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 7, "FOO", BigDecimal.valueOf(2), false);

        assertEquals(BigDecimal.valueOf(546).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-854).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-854).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testShortPutExpectExpirationValue() throws Exception {
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, -3, "FOO", BigDecimal.valueOf(2), false);

        assertEquals(BigDecimal.valueOf(-246).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(354).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(354).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void givenShortPut_whenGetGreeks_expectCorrectSign() {
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, -1, "FOO", BigDecimal.valueOf(2), false);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), greaterThan(0d));
        assertThat(greeks.getTheta(), greaterThan(0d));
        assertThat(greeks.getGamma(), lessThan(0d));
        assertThat(greeks.getVega(), lessThan(0d));
    }

    @Test
    public void givenLongPut_whenGetGreeks_expectCorrectSign() {
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 1, "FOO", BigDecimal.valueOf(2), false);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), lessThan(0d));
        assertThat(greeks.getTheta(), lessThan(0d));
        assertThat(greeks.getGamma(), greaterThan(0d));
        assertThat(greeks.getVega(), greaterThan(0d));
    }

    @Test
    public void givenLongCall_whenGetGreeks_expectCorrectSign() {
        Option option = new CallOption("FOO", currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 1, "FOO", BigDecimal.valueOf(2), false);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), greaterThan(0d));
        assertThat(greeks.getTheta(), lessThan(0d));
        assertThat(greeks.getGamma(), greaterThan(0d));
        assertThat(greeks.getVega(), greaterThan(0d));
    }
    @Test
    public void givenShortCall_whenGetGreeks_expectCorrectSign() {
        Option option = new CallOption("FOO", currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, -1, "FOO", BigDecimal.valueOf(2), false);

        final Greeks greeks = optionTrade.getGreeks();
        assertThat(greeks.getDelta(), lessThan(0d));
        assertThat(greeks.getTheta(), greaterThan(0d));
        assertThat(greeks.getGamma(), lessThan(0d));
        assertThat(greeks.getVega(), lessThan(0d));
    }
}
