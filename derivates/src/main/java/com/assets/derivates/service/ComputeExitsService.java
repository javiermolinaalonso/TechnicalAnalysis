package com.assets.derivates.service;

import com.assets.statistic.list.StockList;

public interface ComputeExitsService {

    public StockList computeExits(StockList entryPoints, StockList input);
    
}
