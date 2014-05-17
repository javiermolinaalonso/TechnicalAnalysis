package com.assets.portfolio.correlation.entities.stock.exceptions;

import java.time.Instant;

public class StockNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 249075235565443036L;
	private final Instant instant;

	public StockNotFoundException(Instant instant) {
		super();
		this.instant = instant;
	}

	@Override
	public String getMessage() {
		return String.format("Instant %s not found", instant.toString());
	}
}
