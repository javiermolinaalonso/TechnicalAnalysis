package com.assets.options.entities;

import com.assets.options.entities.spread.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class OptionSpreadTest {

    private final BigDecimal comission = BigDecimal.valueOf(2);

    private final BigDecimal currentValue = BigDecimal.valueOf(8800);
    private final BigDecimal strike8600 = BigDecimal.valueOf(8600);
    private final BigDecimal strike8800 = BigDecimal.valueOf(8800);
    private final BigDecimal strike9000 = BigDecimal.valueOf(9000);
    private final BigDecimal strike9200 = BigDecimal.valueOf(9200);

    private final LocalDate now = LocalDate.of(2015, 1, 1);
    private final LocalDate twoMonths = LocalDate.of(2015, 3, 1);
    private final LocalDate fourMonths = LocalDate.of(2015, 5, 1);
    private final LocalDate sixMonths = LocalDate.of(2015, 7, 1);

    private final Double volatility = 0.2d;
    private final Double riskFree = 0.01d;

    @Test
    public void testGivenBullCallSpread() throws Exception {
        BaseOptionSpread optionSpread = new BullCallSpread(currentValue, strike8800, strike9000, now, twoMonths, volatility, riskFree, comission, "FOO", 1, true);
        printSpread(optionSpread);
        assertEquals(-91, optionSpread.getMaxLoss().doubleValue(), 1d);
        assertEquals(108, optionSpread.getMaxGain().doubleValue(), 1d);
    }

    @Test
    public void testGivenBearCallSpread() throws Exception {
        BaseOptionSpread optionSpread = new BearCallSpread(currentValue, strike8600, strike8800, now, twoMonths, volatility, riskFree, comission, "FOO", 5, true);
        printSpread(optionSpread);
    }

    @Test
    public void testGivenBullPutSpread() throws Exception {
        BaseOptionSpread optionSpread = new BullPutSpread(currentValue, strike9000, strike9200, now, twoMonths, volatility, riskFree, comission, "FOO", 5, true);
        printSpread(optionSpread);
    }

    @Test
    public void testGivenIronCondorSpread() throws Exception {
        IronCondorSpread optionSpread = new IronCondorSpread(currentValue, strike8600, strike8800, strike9000, strike9200, now, twoMonths, volatility, riskFree, comission, "FOO", 5, true);
        printSpread(optionSpread);
    }

    private void printSpread(BaseOptionSpread optionSpread) {
        for(double expectedPrice = 8400; expectedPrice < 9400; expectedPrice+=50) {
            BigDecimal expectedValue = optionSpread.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            System.out.println(String.format("%.5f", expectedValue.doubleValue()));
        }
    }
}
