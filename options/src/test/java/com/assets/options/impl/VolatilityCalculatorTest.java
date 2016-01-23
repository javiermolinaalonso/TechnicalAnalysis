package com.assets.options.impl;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.entities.Candlestick;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VolatilityCalculatorTest {

    private List<Candlestick> candlesticks;

    VolatilityCalculator volatilityCalculator;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getClassLoader().getResource("IBEXOneYear.csv");

        DataDailyLoaderCsv loader = new DataDailyLoaderCsv();
        candlesticks = loader.loadData(resource.getPath());

        volatilityCalculator = new VolatilityCalculator();
    }

    @Test
    public void testGivenIbexWhenCalculateVolatilityExpectCorrect() throws Exception {
        assertEquals(0.2135, volatilityCalculator.getAnnualizedVolatility(candlesticks), 0.01d);
    }
}