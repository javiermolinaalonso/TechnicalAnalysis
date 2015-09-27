package com.assets.bollinger.service.impl;

import com.assets.bollinger.data.entities.BollingerValue;
import com.assets.bollinger.service.BollingerService;
import com.assets.statistic.list.StockList;

import java.time.Instant;

public class BollingerServiceImpl implements BollingerService {

	@Override
	public BollingerValue computeInstantBollingerValues(StockList stockList, Instant instant) {
		return null;
	}

}
