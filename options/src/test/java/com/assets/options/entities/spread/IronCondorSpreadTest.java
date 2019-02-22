package com.assets.options.entities.spread;

import com.assets.options.PrintUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IronCondorSpreadTest {

    @Test
    public void givenIronCondorSpread_whenGetMaxProfit_expectCorrect() {
        final IronCondorSpread spy = new IronCondorSpread(278.8, 273, 275, 285, 287,
                LocalDate.now(), LocalDate.now().plusDays(21), 0.11, 0.01, BigDecimal.ONE, "SPY", 1, false);

        PrintUtils.print(spy, 0.05);

        System.out.println();
        System.out.println("Gain: " + spy.getMaxGain());
        System.out.println("Loss: " + spy.getMaxLoss());
    }
}
