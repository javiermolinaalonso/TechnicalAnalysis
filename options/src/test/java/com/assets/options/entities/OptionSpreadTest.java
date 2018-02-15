package com.assets.options.entities;

import com.assets.options.entities.spread.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class OptionSpreadTest {

    private final BigDecimal comission = BigDecimal.valueOf(2);

    private final BigDecimal currentValue = BigDecimal.valueOf(8800);
    private final BigDecimal strike8600 = BigDecimal.valueOf(8600);
    private final BigDecimal strike8800 = BigDecimal.valueOf(8800);
    private final BigDecimal strike9000 = BigDecimal.valueOf(9000);
    private final BigDecimal strike9200 = BigDecimal.valueOf(9200);

    private final LocalDate now = LocalDate.of(2015, 1, 1);
    private final LocalDate oneMonth = LocalDate.of(2015, 2, 1);
    private final LocalDate twoMonths = LocalDate.of(2015, 3, 1);
    private final LocalDate fourMonths = LocalDate.of(2015, 5, 1);
    private final LocalDate sixMonths = LocalDate.of(2015, 7, 1);

    private final Double volatility = 0.2d;
    private final Double riskFree = 0.01d;

    @Test
    public void testGivenBullCallSpread() throws Exception {
        BaseOptionSpread spread = new BullCallSpread(currentValue, strike8800, strike9000, now, twoMonths, volatility, riskFree, comission, "FOO", 1, true);
        assertEquals(91, spread.getCost().doubleValue(), 1d);
        assertEquals(-91, spread.getMaxLoss().doubleValue(), 1d);
        assertEquals(108, spread.getMaxGain().doubleValue(), 1d);
        assertEquals(-91, spread.getExpirationValue(BigDecimal.valueOf(8800)).doubleValue(), 1d);
        assertEquals(-41, spread.getExpirationValue(BigDecimal.valueOf(8850)).doubleValue(), 1d);
        assertEquals(8, spread.getExpirationValue(BigDecimal.valueOf(8900)).doubleValue(), 1d);
        assertEquals(58, spread.getExpirationValue(BigDecimal.valueOf(8950)).doubleValue(), 1d);
        assertEquals(108, spread.getExpirationValue(BigDecimal.valueOf(9000)).doubleValue(), 1d);
        assertEquals(0.11, spread.getGreeks().getDelta(), 0.001d);
        assertEquals(0, spread.getGreeks().getGamma(), 0.0001d);
    }

    @Test
    public void givenBullCallSpread_whenGetGreeksAtMiddleTime_expectCorrectGreeks() throws Exception {
        BaseOptionSpread spread = new BullCallSpread(currentValue, strike8800, strike9000, now, twoMonths, volatility, riskFree, comission, "FOO", 1, true);
        printSpread(spread);

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
        System.out.println(optionSpread.getGreeks());
    }

    private void printSpread(BaseOptionSpread spread) {
        BigDecimal min = BigDecimal.valueOf(999999);
        BigDecimal max = BigDecimal.valueOf(-999999);
        for(double expectedPrice = 8400; expectedPrice < 9400; expectedPrice+=50) {
            BigDecimal expectedValue = spread.getValueAt(BigDecimal.valueOf(8840), oneMonth);
            if (expectedValue.compareTo(min)>0) {
                min = expectedValue;
            }
            if (expectedValue.compareTo(max) < 0) {
                max = expectedValue;
            }
        }
        System.out.println("Min " +min.doubleValue() );
        System.out.println("Max " +max.doubleValue() );
    }
}
