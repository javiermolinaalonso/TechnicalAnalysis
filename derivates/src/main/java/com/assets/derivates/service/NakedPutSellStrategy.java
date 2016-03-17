package com.assets.derivates.service;

import com.assets.derivates.strategies.NakedPutVolatilityStrategyResult;

import java.util.List;

public interface NakedPutSellStrategy<T> {

    NakedPutVolatilityStrategyResult calculateStrategy(List<T> values, int step, double volatilityStart, double strikeDistance, double volatilityEnd);

}
