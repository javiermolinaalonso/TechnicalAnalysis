package com.assets.options;

import com.assets.options.entities.spread.OptionSpread;

import java.math.BigDecimal;

public class PrintUtils {
    public static final int COLUMNS = 100;
    public static final int ROWS = 30;
    public static final double STRIKE_DISTANCE_PERCENT = 0.05d;

    public static void print(OptionSpread spread) {
        final BigDecimal mean = spread.getStrikePriceAverage();
        double lowStrike = mean.doubleValue() - mean.doubleValue() * STRIKE_DISTANCE_PERCENT;
        double hiStrike = mean.doubleValue() + mean.doubleValue() * STRIKE_DISTANCE_PERCENT;
        int[][] matrix = new int[COLUMNS][ROWS];
        double xresolution = ((hiStrike - lowStrike) * 1d / COLUMNS);

        BigDecimal min = BigDecimal.valueOf(999999);
        BigDecimal max = BigDecimal.valueOf(-999999);
        for (double expectedPrice = lowStrike; expectedPrice <= hiStrike; expectedPrice += xresolution) {
            BigDecimal expectedValue = spread.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            if (expectedValue.compareTo(min) < 0) {
                min = expectedValue;
            }
            if (expectedValue.compareTo(max) > 0) {
                max = expectedValue;
            }
        }
        double yresolution = ((max.doubleValue() - min.doubleValue()) * 1d / ROWS);

        int i = 0;
        for (double expectedPrice = lowStrike; expectedPrice < (hiStrike - (xresolution / 4)); expectedPrice += xresolution) {
            final BigDecimal expirationValue = spread.getExpirationValue(BigDecimal.valueOf(expectedPrice));
            int j = 0;
            final boolean medium = Math.abs(spread.getStrikePriceAverage().doubleValue() - expectedPrice) <= xresolution / 2;
            for (double value = max.doubleValue(); value > (min.doubleValue() + yresolution / 2); value -= yresolution) {
                if (medium) matrix[i][j] = 3;
                if (Math.abs(value) <= yresolution / 2) matrix[i][j] = 2;
                if (Math.abs(value - expirationValue.doubleValue()) <= yresolution) matrix[i][j] = 1;
                j++;
            }
            i++;
        }

        for (int j = 0; j < ROWS; j++) {
            for (i = 0; i < COLUMNS; i++) {

                final int value = matrix[i][j];
                if (value == 1) {
                    System.out.print("*");
                } else if (value == 2) {
                    System.out.print("-");
                } else if (value == 3) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

}
