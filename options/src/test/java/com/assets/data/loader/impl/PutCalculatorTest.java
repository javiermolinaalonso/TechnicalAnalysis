package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;
import com.assets.options.PutCalculator;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PutCalculatorTest {

    DataDailyLoaderCsv loader;
    List<Candlestick> candlesticks;

    PutCalculator calculator;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getClassLoader().getResource("IBEXOneYear.csv");

        loader = new DataDailyLoaderCsv();
        candlesticks = loader.loadData(resource.getPath());

        calculator = new PutCalculator();
    }

    @Test
    public void testGivenIbexOneYearWhenCalculatePutExpectCorrectValue() throws Exception {
        Double targetPrice = 8000d;
        LocalDate expirationTime = LocalDate.of(2016, 5, 12);

        Collections.reverse(candlesticks);
        double putPrice = calculator.getPutPrice(candlesticks, expirationTime, targetPrice);

        double expectedPrice = 302.65d;

        assertEquals(expectedPrice, putPrice, 1d);
    }
}