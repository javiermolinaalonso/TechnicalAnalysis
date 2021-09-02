package com.assets.data.loader.impl;

import org.jooq.tools.csv.CSVReader;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.num.DoubleNum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NasdaqDailyLoaderCsv {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public BarSeries loadData(String resource) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(new File(resource)))) {
            List<String[]> r = reader.readAll();
            List<Bar> bars = r.stream().map(values -> new BaseBar(
                    Duration.ofDays(1),
                    LocalDate.parse(values[0], formatter).atTime(LocalTime.of(22, 0)).atZone(ZoneId.systemDefault()),
                    DoubleNum.valueOf(values[3]),
                    DoubleNum.valueOf(values[4]),
                    DoubleNum.valueOf(values[5]),
                    DoubleNum.valueOf(values[1]),
                    DoubleNum.valueOf(values[2]),
                    DoubleNum.valueOf(0)
            ))
                    .sorted(Comparator.comparing(BaseBar::getEndTime))
                    .collect(Collectors.toList());
            return new BaseBarSeriesBuilder()
                    .withBars(bars)
                    .withName("SPY")
                    .build();
        }
    }

}
