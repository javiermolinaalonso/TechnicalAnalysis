package com.assets.data.loader;

import com.assets.statistic.list.StockList;

import java.util.Map;


public interface DataLoader {

    StockList loadData(String ticker);
    
}
