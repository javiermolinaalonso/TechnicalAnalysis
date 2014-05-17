package com.assets.bollinger.service.impl;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import com.assets.portfolio.correlation.entities.stock.StockList;
import com.assets.portfolio.correlation.entities.stock.StockPrice;

public class BollingerServiceImplTest {

	private static final String TICKER = "TEST";
	
	private BollingerServiceImpl bollingerService;
	private StockList values;
	
	@Before
	public void setUp() throws Exception {
		bollingerService = new BollingerServiceImpl();
		values = new StockList(TICKER);
		values.add(new StockPrice(TICKER, Instant.ofEpochSecond(100), BigDecimal.valueOf(10d)));
		values.add(new StockPrice(TICKER, Instant.ofEpochSecond(110), BigDecimal.valueOf(11d)));
		values.add(new StockPrice(TICKER, Instant.ofEpochSecond(120), BigDecimal.valueOf(12d)));
		values.add(new StockPrice(TICKER, Instant.ofEpochSecond(130), BigDecimal.valueOf(13d)));
		values.add(new StockPrice(TICKER, Instant.ofEpochSecond(140), BigDecimal.valueOf(12d)));
	}

	@Test(expected=)
	public void testInstantNotExist() {
		boll
		fail("Not yet implemented");
	}

}
