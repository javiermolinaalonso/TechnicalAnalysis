package com.assets.options;

import com.assets.options.entities.spread.IronCondorSpread;
import com.assets.options.entities.spread.VerticalCallSpread;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionsPrinter {

    public static void main(String[] args) {

        final IronCondorSpread ironCondor = new IronCondorSpread(277.41, 265d, 270d, 290d, 295d,
                LocalDate.now(),
                LocalDate.of(2019, 3, 15),
                0.15, 0.01d, BigDecimal.ONE, "SPY", 1, false
        );
        System.out.println(ironCondor.getMaxLoss());
        System.out.println(ironCondor.getMaxGain());
        System.out.println(ironCondor.getCost());
        PrintUtils.print(ironCondor, 0.005);
    }

}
