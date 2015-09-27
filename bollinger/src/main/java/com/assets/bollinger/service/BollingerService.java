package com.assets.bollinger.service;

import com.assets.bollinger.data.entities.BollingerValue;
import com.assets.statistic.list.StockList;

import java.time.Instant;

public interface BollingerService {

	public BollingerValue computeInstantBollingerValues(StockList stockList, Instant instant);
	
}
