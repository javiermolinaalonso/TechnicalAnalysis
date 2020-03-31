package com.assets.options.book.loader.ib;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class IBOptionDataResponse {

    private final long conid;
    private final String symbol;
    private final String secType;
    private final String exchange;
    private final String right;
    private final String strike;
    private final String currency;
    private final String maturityDate;
    private final String multiplier;

    public IBOptionDataResponse(
            @JsonProperty("conid") long conid,
            @JsonProperty("symbol") String symbol,
            @JsonProperty("secType") String secType,
            @JsonProperty("exchange") String exchange,
            @JsonProperty("right") String right,
            @JsonProperty("strike") String strike,
            @JsonProperty("currency") String currency,
            @JsonProperty("maturiyDate") String maturityDate,
            @JsonProperty("multiplier") String multiplier) {
        this.conid = conid;
        this.symbol = symbol;
        this.secType = secType;
        this.exchange = exchange;
        this.right = right;
        this.strike = strike;
        this.currency = currency;
        this.maturityDate = maturityDate;
        this.multiplier = multiplier;
    }

    public long getConid() {
        return conid;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSecType() {
        return secType;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRight() {
        return right;
    }

    public String getStrike() {
        return strike;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public String getMultiplier() {
        return multiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IBOptionDataResponse that = (IBOptionDataResponse) o;

        return new EqualsBuilder()
                .append(conid, that.conid)
                .append(symbol, that.symbol)
                .append(secType, that.secType)
                .append(right, that.right)
                .append(strike, that.strike)
                .append(maturityDate, that.maturityDate)
                .append(multiplier, that.multiplier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conid)
                .append(symbol)
                .append(secType)
                .append(right)
                .append(strike)
                .append(maturityDate)
                .append(multiplier)
                .toHashCode();
    }
}
