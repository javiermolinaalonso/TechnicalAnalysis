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

    private Double riskFree = 0.1d;

    public static OptionBuilder create(String ticker, double currentPrice) {
        return create(ticker,currentPrice,Clock.systemUTC());
    }

    public static OptionBuilder create(String ticker, double currentPrice, Clock clock) {
        return new OptionBuilder(ticker, currentPrice, clock);
    }

    private OptionBuilder(String ticker, double currentPrice, Clock clock) {
        this.ticker = ticker;
        this.currentPrice = BigDecimal.valueOf(currentPrice);
        this.clock = clock;
    }

    public OptionBuilder withStrikePrice(double strikePrice) {
        this.strikePrice = BigDecimal.valueOf(strikePrice);
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

    public OptionBuilder withIV(double iv){
        this.impliedVolatility = iv;
        return this;
    }

    public OptionBuilder withRiskFree(double riskFree) {
        this.riskFree = riskFree;
        return this;
    }

    public CallOption buildCall() {
        if (impliedVolatility == null) {
            return new CallOption(ticker, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, riskFree);
        } else {
            return new CallOption(ticker, currentPrice, strikePrice, currentDate, expirationDate, impliedVolatility, riskFree);
        }
    }

    public PutOption buildPut() {
        if (impliedVolatility == null) {
            return new PutOption(ticker, currentPrice, strikePrice, bid, ask, currentDate, expirationDate, riskFree);
        } else {
            return new PutOption(ticker, currentPrice, strikePrice, currentDate, expirationDate, impliedVolatility, riskFree);
        }
    }
}
