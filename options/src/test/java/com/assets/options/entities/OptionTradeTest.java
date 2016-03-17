package com.assets.options.entities;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionTradeTest {

    LocalDate firstTrade;
    LocalDate expirationDate;

    double volatility = 0.2d;

    @Before
    public void setUp() throws Exception {
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
            System.out.println(foo.getCurrentValue(currentPrice, date, volatility));
        }
    }
}
