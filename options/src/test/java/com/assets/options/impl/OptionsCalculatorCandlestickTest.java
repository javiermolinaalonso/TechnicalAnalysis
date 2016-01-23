package com.assets.options.impl;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.entities.Candlestick;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OptionsCalculatorCandlestickTest {

    DataDailyLoaderCsv loader;
    List<Candlestick> candlesticks;

    OptionsCalculatorCandlestick calculator;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getClassLoader().getResource("IBEXOneYear.csv");

        loader = new DataDailyLoaderCsv();
        candlesticks = loader.loadData(resource.getPath());

        VolatilityCalculator volatilityCalculator = new VolatilityCalculator();
        calculator = new OptionsCalculatorCandlestick(volatilityCalculator);
    }

    @Test
    public void testGivenIbexOneYearWhenCalculatePutExpectCorrectValue() throws Exception {
        Double targetPrice = 8000d;
        LocalDate expirationTime = LocalDate.of(2016, 5, 12);

        List<Candlestick> values = getValues(23);
        double putPrice = calculator.put(values, expirationTime, targetPrice);

        double expectedPrice = 274.09;

        assertEquals(expectedPrice, putPrice, 1d);
    }

    private List<Candlestick> getValues(int maxIndex) {
        LinkedList<Candlestick> values = new LinkedList<>();
        for(int i = 0; i < maxIndex; i++) {
            values.addFirst(candlesticks.get(i));
        }
        return values;
    }
}