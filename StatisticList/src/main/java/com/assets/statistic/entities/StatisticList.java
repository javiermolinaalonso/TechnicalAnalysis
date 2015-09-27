package com.assets.statistic.entities;

import java.math.BigDecimal;
import java.util.List;


public interface StatisticList<T> {

    T getMean();
    
    T getHighest();
    
    T getLowest();
    
    T getStdDev();
    
    List<BigDecimal> getList();
}
