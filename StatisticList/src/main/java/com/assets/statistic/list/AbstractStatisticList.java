package com.assets.statistic.list;

import com.assets.statistic.entities.StatisticList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStatisticList<T> implements StatisticList<BigDecimal> {

    protected final List<T> list;

    public AbstractStatisticList() {
        this.list = new ArrayList<>();
    }

    public AbstractStatisticList(List<T> origin){
        this.list = origin;
    }
 
    public BigDecimal getMean() {
        if(list.isEmpty()){
            return BigDecimal.ZERO;
        }
        return computeMean();
    }

    protected abstract BigDecimal computeMean();
}
