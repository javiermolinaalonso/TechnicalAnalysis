package com.investment.strategy;

import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PriceReachesTargetTest {

    public static final String TICKER = "Foo";
    private StockList stockList;

    @Before
    public void setUp() throws Exception {
        stockList = new StockList(TICKER);
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 1, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.85)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 2, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.95)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 3, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.90)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 4, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.70)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 5, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.72)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 8, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.50)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 9, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.20)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 10, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(11.7)));
        stockList.add(new StockPrice(TICKER, LocalDateTime.of(2016, 1, 11, 0, 0).toInstant(ZoneOffset.UTC), BigDecimal.valueOf(12.00)));
    }

    @Test
    public void testGivenStockListWhenDetermineActionExpectFalse() throws Exception {


    }
}
