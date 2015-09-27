package com.assets.portfolio.data.loader;

import com.assets.statistic.list.StockList;

import java.util.Map;


public interface DataLoader {

    Map<String, StockList> loadData();
    
    Map<String, StockList> loadData(Integer amount);
    
    StockList loadStockList(String ticker);
    
    void setDataFile(String dataFile);
}
