package com.assets.portfolio.entities;

import com.assets.entities.StatisticListType;
import com.assets.statistic.entities.FactoryStatisticList;
import com.assets.statistic.entities.StatisticList;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertTrue;

@BenchmarkOptions(benchmarkRounds=1, warmupRounds=1)
public class TestBenchmarkStatisticList {

    private static final int MAX_VALUES = 1000000;

    @Rule
    public TestRule benchmark = new BenchmarkRule();
    
    private static BigDecimal[] array = new BigDecimal[MAX_VALUES]; 
    private static BigDecimal result;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < MAX_VALUES; i++){
            array[i] = new BigDecimal(r.nextDouble());
        }
        result = FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA).getMean();
    }

    @Before
    public void setUp() {
        
    }
    
    @Test
    public void arrayListSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void linkedListSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new LinkedList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void vectorSingleThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new Vector<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }
    
    @Test
    public void arrayListMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new ArrayList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void linkedListMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new LinkedList<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }

    @Test
    public void vectorMultiThread() throws Exception {
        runTest(FactoryStatisticList.getStatisticList(new Vector<BigDecimal>(Arrays.asList(array)), StatisticListType.LAMBDA));
    }
    
    public void runTest(StatisticList<BigDecimal> sList) {
        BigDecimal r = sList.getMean();
        assertTrue(result.equals(r));
    }

}
