package com.assets.statistic.list;

import com.assets.entities.StatisticListType;
import com.assets.statistic.entities.FactoryStatisticList;
import com.assets.statistic.entities.StatisticList;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestStatisticList {

    private StatisticList<BigDecimal> sList;
    private List<BigDecimal> prices;
    
    @Before
    public void setUp() throws Exception {
        prices = new ArrayList<>();
        prices.add(BigDecimal.valueOf(10d));
        prices.add(BigDecimal.valueOf(11d));
        prices.add(BigDecimal.valueOf(12d));
        prices.add(BigDecimal.valueOf(13d));
        prices.add(BigDecimal.valueOf(14d));
        sList = FactoryStatisticList.getStatisticList(prices, StatisticListType.LAMBDA);
    }

    @Test
    public void testMean() {
        assertEquals(12d, sList.getMean().doubleValue(), 0.001d);
    }
    
    @Test
    public void testMaximum() {
        assertEquals(BigDecimal.valueOf(14d), sList.getHighest());
    }
    
    @Test
    public void testMinimum() {
        assertEquals(BigDecimal.valueOf(10d), sList.getLowest());
    }
    
    @Test
    public void testStdDev() {
        assertEquals(1.414213562d, sList.getStdDev().doubleValue(), 0.001d);
    }
}
