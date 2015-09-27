package com.assets.statistics.entities;

import com.assets.statistic.entities.StatisticList;

/**
 * Created by javier on 27/09/15.
 */
public interface CorrelationStatisticList<T> extends StatisticList<T> {

    T getCorrelation(StatisticList<T> otherList);

    T getCovariance(StatisticList<T> otherList);

}
