package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Created by javier on 28/09/15.
 */
public class HistDataCsvFileLoader extends AbstractCsvLoader<Candlestick> {

    /*
        The format of the HistData is
        YYYY.MM.DD,HH:mm,open,max,min,close
     */
    protected Candlestick parseLine(String[] data) {

        int year = Integer.valueOf(data[0].split("\\.")[0]);
        int month = Integer.valueOf(data[0].split("\\.")[1]);
        int day = Integer.valueOf(data[0].split("\\.")[2]);

        int hour = Integer.valueOf(data[1].split(":")[0]);
        int minute = Integer.valueOf(data[1].split(":")[1]);
        LocalDateTime dt = LocalDateTime.of(year, month, day, hour, minute);
        Instant instant = dt.toInstant(ZoneOffset.UTC);

        return new Candlestick.Builder()
                .withInitialInstant(instant)
                .withDuration(Duration.of(1, ChronoUnit.MINUTES))
                .withInitialPrice(BigDecimal.valueOf(Double.parseDouble(data[2])))
                .withMaxPrice(BigDecimal.valueOf(Double.parseDouble(data[3])))
                .withMinPrice(BigDecimal.valueOf(Double.parseDouble(data[4])))
                .withFinalPrice(BigDecimal.valueOf(Double.parseDouble(data[5])))
                .build();
    }

}
