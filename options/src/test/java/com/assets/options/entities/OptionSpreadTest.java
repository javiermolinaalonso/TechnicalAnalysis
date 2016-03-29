package com.assets.options.entities;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class OptionSpreadTest {

    private final BigDecimal comission = BigDecimal.valueOf(2);

    private final BigDecimal currentValue = BigDecimal.valueOf(8800);
    private final BigDecimal strike8600 = BigDecimal.valueOf(8600);
    private final BigDecimal strike8800 = BigDecimal.valueOf(8800);
    private final BigDecimal strike9000 = BigDecimal.valueOf(9000);

    private final LocalDate now = LocalDate.of(2015, 1, 1);
    private final LocalDate twoMonths = LocalDate.of(2015, 3, 1);
    private final LocalDate fourMonths = LocalDate.of(2015, 5, 1);
    private final LocalDate sixMonths = LocalDate.of(2015, 7, 1);

    private final Double volatility = 0.2d;
    private final Double riskFree = 0.01d;

    @Test
    public void testGivenBullCallSpread() throws Exception {
        Option lowerCallOption = new CallOption(currentValue, strike8800, now, twoMonths, volatility, riskFree);
        Option upperCallOption = new CallOption(currentValue, strike9000, now, twoMonths, volatility, riskFree);
        OptionTrade lowerCallOptionTrade = new OptionTrade(lowerCallOption, lowerCallOption.getPremium(), 5, "FOO", comission, true);
        OptionTrade upperCallOptionTrade = new OptionTrade(upperCallOption, upperCallOption.getPremium(), -5, "FOO", comission, true);

        OptionSpread optionSpread = new OptionSpread(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));

        printSpread(optionSpread);
    }

    @Test
    public void testGivenBearCallSpread() throws Exception {
        Option lowerCallOption = new CallOption(currentValue, strike8600, now, twoMonths, volatility, riskFree);
        Option upperCallOption = new CallOption(currentValue, strike8800, now, twoMonths, volatility, riskFree);
        OptionTrade lowerCallOptionTrade = new OptionTrade(lowerCallOption, lowerCallOption.getPremium(), -5, "FOO", comission, true);
        OptionTrade upperCallOptionTrade = new OptionTrade(upperCallOption, upperCallOption.getPremium(), 5, "FOO", comission, true);

        OptionSpread optionSpread = new OptionSpread(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));

        printSpread(optionSpread);
    }

    private void printSpread(OptionSpread optionSpread) {
        for(double expectedPrice = 8400; expectedPrice < 9400; expectedPrice+=50) {
            BigDecimal expectedValue = optionSpread.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedValue.doubleValue()));
        }
    }
}
