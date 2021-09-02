package com.assets.options;

import java.time.LocalDate;
import java.time.Month;

public class OptionUtils {

    public static LocalDate getThirdFridayOfMonth(Integer year, Month month) {
        LocalDate d = LocalDate.of(year, month, 1);
        int ordinal = d.getDayOfWeek().ordinal();
        int offsetDays = 15;
        if (ordinal > 4) {
            offsetDays += 7 - (ordinal - 4);
        } else if (ordinal < 4) {
            offsetDays += 4 - ordinal;
        }
        return LocalDate.of(year, month, offsetDays);
    }

}
