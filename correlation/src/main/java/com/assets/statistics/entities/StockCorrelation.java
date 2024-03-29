package com.assets.statistics.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class StockCorrelation implements Comparable<StockCorrelation> {

    private final String tickerOne;
    private final String tickerTwo;
    private final BigDecimal correlation;
    private final Instant from;
    private final Instant to;
    private final Integer comparedValues;
    private final CorrelationStatisticList<BigDecimal> firstStockValues;
    private final CorrelationStatisticList<BigDecimal> secondStockValues;

    public StockCorrelation(String tickerOne, String tickerTwo, BigDecimal correlation, Instant from, Instant to, Integer comparedValues, CorrelationStatisticList<BigDecimal> firstStockValues, CorrelationStatisticList<BigDecimal> secondStockValues) {
        super();
        this.tickerOne = tickerOne;
        this.tickerTwo = tickerTwo;
        this.correlation = correlation;
        this.from = from;
        this.to = to;
        this.comparedValues = comparedValues;
        this.firstStockValues = firstStockValues;
        this.secondStockValues = secondStockValues;
    }


    @Override
    public int compareTo(StockCorrelation o) {
        return correlation.compareTo(o.getCorrelation());
    }


    public String getTickerOne() {
        return this.tickerOne;
    }


    public String getTickerTwo() {
        return this.tickerTwo;
    }


    public BigDecimal getCorrelation() {
        return this.correlation;
    }


    public Instant getFrom() {
        return this.from;
    }


    public Instant getTo() {
        return this.to;
    }


    public Integer getComparedValues() {
        return this.comparedValues;
    }

    public CorrelationStatisticList<BigDecimal> getFirstStockValues() {
        return firstStockValues;
    }

    public CorrelationStatisticList<BigDecimal> getSecondStockValues() {
        return secondStockValues;
    }

    @Override
    public String toString(){
        return String.format("Covariance of %s against %s is %s in period %s to %s with %s values", tickerOne, tickerTwo, correlation.setScale(2, RoundingMode.HALF_DOWN).toString(), from.toString(), to.toString(), comparedValues);
    }

}
