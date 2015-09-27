package com.assets.trades;


import com.assets.entities.StockPrice;

public interface BuyStrategy {

    public Integer getSharesToBuy(StockPrice price);
    
}
