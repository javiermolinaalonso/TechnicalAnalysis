package com.assets.options.entities;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class OptionSpreadTest {

    private final BigDecimal currentValue = BigDecimal.valueOf(8950);
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
        OptionTrade lowerCallOptionTrade = new OptionTrade(lowerCallOption, lowerCallOption.getPremium(), 1, "FOO");
        OptionTrade upperCallOptionTrade = new OptionTrade(upperCallOption, lowerCallOption.getPremium(), -1, "FOO");

        OptionSpread optionSpread = new OptionSpread(Arrays.asList(lowerCallOptionTrade, upperCallOptionTrade));

        for(double expectedPrice = 8000; expectedPrice < 10000; expectedPrice+=50) {
            BigDecimal expectedValue = optionSpread.getExpectedValue(BigDecimal.valueOf(expectedPrice), twoMonths.minusDays(1), volatility);
            System.out.println(String.format("%.5f", expectedValue.doubleValue()));
        }

    }
}
