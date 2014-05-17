package com.assets.bollinger.service.impl;

import java.time.Instant;

import com.assets.bollinger.data.entities.BollingerValue;
import com.assets.bollinger.service.BollingerService;
import com.assets.portfolio.correlation.entities.stock.StockList;

public class BollingerServiceImpl implements BollingerService {

	@Override
	public BollingerValue computeInstantBollingerValues(StockList stockList, Instant instant) {
		return null;
	}

}
