package com.assets.options.entities;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class OptionTradeTest {

    public static final double MIN = 9;
    public static final double MAX = 12.5d;
    public static final double STEP = 0.5;
    public static final int STRIKE_PRICE = 11;

    LocalDate firstTrade;
    LocalDate expirationDate;

    double volatility = 0.2d;
    BigDecimal currentPrice = BigDecimal.valueOf(10);
    BigDecimal callPrice = BigDecimal.valueOf(0.5);
    BigDecimal putPrice = BigDecimal.valueOf(1.2);

    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.US);
        this.firstTrade = LocalDate.of(2016, 1, 5);
        this.expirationDate = LocalDate.of(2016, 3, 15);
    }

    @Test
    public void testRealValues() throws Exception {
        LocalDate expiration = LocalDate.of(2016, 6, 17);
        BigDecimal currentValue = BigDecimal.valueOf(4.06);
        BigDecimal strike = BigDecimal.valueOf(4.75);
        Option option = new CallOption(currentValue, strike, LocalDate.now(), expiration, 0.4d, 0.001);
        System.out.println(String.format("Current value: %.2f", option.getPremium().doubleValue()));
    }

    @Test
    public void testGivenSameOptionsDifferentEndDateExpectCorrectPrice() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option firstOption = new PutOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade foo = new OptionTrade(firstOption, 10, "FOO", BigDecimal.ZERO, false);

        LocalDate date = LocalDate.of(firstTrade.getYear(), firstTrade.getMonth(), firstTrade.getDayOfMonth());

        while(date.isBefore(expirationDate)) {
            date = date.plusDays(1);
            Option option = foo.getExpectedValue(currentPrice, date, volatility);
            System.out.println(String.format("%.5f, %s", option.getPremium(), option.getGreeks()));
        }
    }

    @Test
    public void testGivenLongCallWhenExpirationValueExpectThreeCorrectPoints() throws Exception {
        Option option = new CallOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, 5, "FOO", BigDecimal.valueOf(2), false);

        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(-260).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
        assertEquals(BigDecimal.valueOf(490).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
    }

    @Test
    public void testGivenShortCallExpectExpirationValue() throws Exception {
        Option option = new CallOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), callPrice, firstTrade, expirationDate, 0d);
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
    public void testShortPutUsingPriceExpectExpirationValue() throws Exception {
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(STRIKE_PRICE), putPrice, firstTrade, expirationDate, 0d);
        OptionTrade optionTrade = new OptionTrade(option, -3, "FOO", BigDecimal.valueOf(2), false);
//        assertEquals(BigDecimal.valueOf(-591).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MIN)).doubleValue(), 0.001d);
//        assertEquals(BigDecimal.valueOf(9).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(STRIKE_PRICE)).doubleValue(), 0.001d);
//        assertEquals(BigDecimal.valueOf(9).doubleValue(), optionTrade.getExpirationValue(BigDecimal.valueOf(MAX)).doubleValue(), 0.001d);
        print(optionTrade);
    }

    private void print(OptionTrade optionTrade) {
        for(double expectedPrice = MIN; expectedPrice <= MAX; expectedPrice+= STEP) {
            BigDecimal expectedPriceOption = optionTrade.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.doubleValue()));
        }
    }


}
