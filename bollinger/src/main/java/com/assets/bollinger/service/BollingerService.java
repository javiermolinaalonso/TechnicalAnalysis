package com.assets.bollinger.service;

import java.time.Instant;

import com.assets.bollinger.data.entities.BollingerValue;
import com.assets.portfolio.correlation.entities.stock.StockList;

public interface BollingerService {

	public BollingerValue computeInstantBollingerValues(StockList stockList, Instant instant);
	
}
