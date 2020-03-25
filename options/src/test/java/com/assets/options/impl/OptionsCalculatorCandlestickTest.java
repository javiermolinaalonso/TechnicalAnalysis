package com.assets.options.impl;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.entities.Candlestick;
import com.assets.options.entities.CallOption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
        BigDecimal targetPrice = BigDecimal.valueOf(8000d);
        LocalDate expirationTime = LocalDate.of(2016, 5, 12);

        List<Candlestick> values = getValues(23);
        BigDecimal putPrice = calculator.put(values, expirationTime, values.get(values.size() - 1).getDate(), targetPrice).getBid();

        double expectedPrice = 218.09;

        assertEquals(expectedPrice, putPrice.doubleValue(), 1d);
    }

    @Test
    public void testfoo() throws Exception {
        BigDecimal strike = BigDecimal.valueOf(9000);
        LocalDate expirationTime = LocalDate.of(2016, 4, 15);
        LocalDate today = LocalDate.of(2016, 3, 15);

        Candlestick todayCandlestick = mock(Candlestick.class);
        Mockito.doReturn(BigDecimal.valueOf(8988)).when(todayCandlestick).getFinalPrice();
        CallOption call = calculator.call(todayCandlestick, expirationTime, today, strike, 0.20);

        System.out.println(call);
    }

    private List<Candlestick> getValues(int maxIndex) {
        LinkedList<Candlestick> values = new LinkedList<>();
        for(int i = 0; i < maxIndex; i++) {
            values.addFirst(candlesticks.get(i));
        }
        return values;
    }
}