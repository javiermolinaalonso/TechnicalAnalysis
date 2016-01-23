package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DataDailyLoaderCsv extends AbstractCsvLoader {

    /*
        The format of the HistData is
        Date,Open,High,Low,Close,Volume,Adj Close
        YYYY-MM-dd,open,max,min,close,volume,adjclose
     */
    private final SimpleDateFormat dateFormat;

    public DataDailyLoaderCsv() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    protected Candlestick parseLine(String[] data) {
        try {
            Date day = dateFormat.parse(data[0]);
            Instant instant = day.toInstant();
            return new Candlestick.Builder()
                    .withInitialInstant(instant)
                    .withDuration(Duration.of(1, ChronoUnit.DAYS))
                    .withInitialPrice(BigDecimal.valueOf(Double.parseDouble(data[1])))
                    .withMaxPrice(BigDecimal.valueOf(Double.parseDouble(data[2])))
                    .withMinPrice(BigDecimal.valueOf(Double.parseDouble(data[3])))
                    .withFinalPrice(BigDecimal.valueOf(Double.parseDouble(data[4])))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
