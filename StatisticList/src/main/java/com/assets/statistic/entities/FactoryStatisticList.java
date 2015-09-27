package com.assets.statistic.entities;

import com.assets.entities.StatisticListType;
import com.assets.statistic.list.LambdaMultithreadStatisticList;
import com.assets.statistic.list.LambdaStatisticList;
import com.assets.statistic.list.SimpleMultithreadStatisticList;
import com.assets.statistic.list.SimpleStatisticList;

import java.math.BigDecimal;
import java.util.List;

public class FactoryStatisticList {

    public static StatisticList<BigDecimal> getStatisticList(List<BigDecimal> list) {
        return getStatisticList(list, StatisticListType.LAMBDA);
    }
    public static StatisticList<BigDecimal> getStatisticList(List<BigDecimal> list, StatisticListType type) {
        switch(type){
        case LAMBDA:
            return new LambdaStatisticList(list);
        case LAMBDA_MULTI:
            return new LambdaMultithreadStatisticList(list);
        case MULTI:
            return new SimpleMultithreadStatisticList(list);
        case SINGLE:
            return new SimpleStatisticList(list);
        default:
            break;
        
        }
        return new LambdaStatisticList(list);
    }

}
