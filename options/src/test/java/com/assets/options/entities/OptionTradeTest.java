package com.assets.options.entities;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public class OptionTradeTest {

    LocalDate firstTrade;
    LocalDate expirationDate;

    double volatility = 0.2d;

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
        OptionTrade foo = new OptionTrade(firstOption, BigDecimal.valueOf(0.2d), 10, "FOO");

        LocalDate date = LocalDate.of(firstTrade.getYear(), firstTrade.getMonth(), firstTrade.getDayOfMonth());

        while(date.isBefore(expirationDate)) {
            date = date.plusDays(1);
            Option option = foo.getExpectedValue(currentPrice, date, volatility);
            System.out.println(String.format("%.5f, %.5f, %.5f, %.5f", option.getPremium(), option.getDelta(), option.getTheta(), option.getGamma()));
        }
    }

    @Test
    public void testShowCurrentValue() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option option = new CallOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade optionTrade = new OptionTrade(option, BigDecimal.valueOf(0.2d), 10, "FOO");

        for(double expectedPrice = 7; expectedPrice < 14; expectedPrice+=0.1) {
            Option expectedPriceOption = optionTrade.getExpectedValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.getPremium().doubleValue()));
        }
    }

    @Test
    public void testGivenLongCallExpectExpirationValue() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option option = new CallOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade optionTrade = new OptionTrade(option, BigDecimal.valueOf(0.05d), 1, "FOO");

        for(double expectedPrice = 5; expectedPrice <= 20; expectedPrice+=0.5) {
            BigDecimal expectedPriceOption = optionTrade.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.doubleValue()));
        }
    }

    @Test
    public void testGivenShortCallExpectExpirationValue() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option option = new CallOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade optionTrade = new OptionTrade(option, BigDecimal.valueOf(0.05d), -1, "FOO");

        for(double expectedPrice = 5; expectedPrice <= 20; expectedPrice+=0.5) {
            BigDecimal expectedPriceOption = optionTrade.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.doubleValue()));
        }
    }

    @Test
    public void testGivenLongPutExpectExpirationValue() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade optionTrade = new OptionTrade(option, BigDecimal.valueOf(0.05d), 1, "FOO");

        for(double expectedPrice = 5; expectedPrice <= 20; expectedPrice+=0.5) {
            BigDecimal expectedPriceOption = optionTrade.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.doubleValue()));
        }
    }

    @Test
    public void testShortPutExpectExpirationValue() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(10);
        Option option = new PutOption(currentPrice, BigDecimal.valueOf(11), firstTrade, expirationDate, volatility, 0d);
        OptionTrade optionTrade = new OptionTrade(option, BigDecimal.valueOf(0.05d), -1, "FOO");

        for(double expectedPrice = 5; expectedPrice <= 20; expectedPrice+=0.5) {
            BigDecimal expectedPriceOption = optionTrade.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedPriceOption.doubleValue()));
        }
    }
}
