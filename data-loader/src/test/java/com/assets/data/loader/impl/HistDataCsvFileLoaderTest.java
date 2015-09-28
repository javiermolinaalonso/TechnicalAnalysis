package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by javier on 28/09/15.
 */
public class HistDataCsvFileLoaderTest {

    private HistDataCsvFileLoader loader;

    @Before
    public void setUp() throws Exception {
        loader = new HistDataCsvFileLoader();
    }

    @Test
    public void testGivenCorrectFileWhenLoadingExpectCorrectLoad() throws IOException {
        //Given
        URL resource = getClass().getClassLoader().getResource("HistDataSample.csv");
        //When
        List<Candlestick> candlesticks = loader.loadData(resource.getPath());

        //Then
        assertEquals(100, candlesticks.size());
    }

    @Test
    public void testGivenSingleValueWhenReadingExpectCorrectCandlestick() throws IOException {
        //Given
        URL resource = getClass().getClassLoader().getResource("SingleCandlestick.csv");

        //When
        Candlestick candlestick = loader.loadData(resource.getPath()).get(0);

        //Then
        assertEquals(Duration.ofMinutes(1), candlestick.getDuration());
        assertEquals(1388596200l, candlestick.getInitialInstant().getEpochSecond());
        assertEquals(1.375300, candlestick.getInitialPrice().doubleValue(), 0.000001);
        assertEquals(1.375990, candlestick.getMaxPrice().doubleValue(), 0.000001);
        assertEquals(1.375200, candlestick.getMinPrice().doubleValue(), 0.000001);
        assertEquals(1.375980, candlestick.getFinalPrice().doubleValue(), 0.000001);
    }
}