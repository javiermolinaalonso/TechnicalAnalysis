package com.assets.options;

import com.assets.options.entities.spread.BullCallSpread;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionsPrinter {

    public static void main(String[] args) {
        final BullCallSpread bullCallSpread = new BullCallSpread(
                BigDecimal.valueOf(14.62),
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(16),
                LocalDate.now(),
                LocalDate.of(2018, 4, 20),
                0.31d,
                0.01d,
                BigDecimal.ONE,
                "GE",
                1,
                false
        );

        System.out.println(bullCallSpread.getMaxLoss());
        System.out.println(bullCallSpread.getMaxGain());
        PrintUtils.print(bullCallSpread, 0.05);
    }
}
