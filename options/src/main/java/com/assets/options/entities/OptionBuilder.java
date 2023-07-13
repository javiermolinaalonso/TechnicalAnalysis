package com.assets.options.entities;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

public class OptionBuilder {

    private final Clock clock;
    private final String ticker;
    private final BigDecimal currentPrice;

    private LocalDate currentDate;
    private BigDecimal strikePrice;
    private LocalDate expirationDate;
    private Double impliedVolatility;
    private BigDecimal bid;
    private BigDecimal ask;

    private String optionSymbol;

    private Double riskFree = 0.001d;

    public static OptionBuilder create(String ticker, double currentPrice) {
        return create(ticker, BigDecimal.valueOf(currentPrice));
    }

    public static OptionBuilder create(String ticker, BigDecimal currentPrice) {
        return create(ticker, currentPrice, Clock.systemUTC());
    }

    public static OptionBuilder create(String ticker, BigDecimal currentPrice, Clock clock) {
        return new OptionBuilder(ticker, currentPrice, clock);
    }

    private OptionBuilder(String ticker, BigDecimal currentPrice, Clock clock) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.clock = clock;
    }

    public OptionBuilder withStrikePrice(double strikePrice) {
        return withStrikePrice(BigDecimal.valueOf(strikePrice));
    }

    public OptionBuilder withStrikePrice(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public OptionBuilder withCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
        return this;
    }

    public OptionBuilder withDaysToExpiry(int daysToExpiry) {
        this.currentDate = LocalDate.now(clock);
        this.expirationDate = currentDate.plusDays(daysToExpiry);
        return this;
    }

    public OptionBuilder withExpirationAt(LocalDate expiration) {
        this.expirationDate = expiration;
        return this;
    }

    public OptionBuilder withBidAsk(double bid, double ask) {
        this.bid = BigDecimal.valueOf(bid);
        this.ask = BigDecimal.valueOf(ask);
        return this;
    }

    public OptionBuilder withPremium(double premium) {
        this.bid = BigDecimal.valueOf(premium);
        this.ask = BigDecimal.valueOf(premium);
        return this;
    }

    public OptionBuilder withIV(double iv) {
        this.impliedVolatility = iv;
        return this;
    }

    public OptionBuilder withRiskFree(double riskFree) {
        this.riskFree = riskFree;
        return this;
    }

    public OptionBuilder withOptionSymbol(String optionSymbol) {
        this.optionSymbol = optionSymbol;
        return this;
    }

    public CallOption buildCall() {
        return new CallOption(ticker, optionSymbol, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, impliedVolatility, riskFree);
    }

    public PutOption buildPut() {
        return new PutOption(ticker, optionSymbol, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, impliedVolatility, riskFree);
    }
}
