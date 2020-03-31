package com.assets.options.book.loader.ib;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IBMarketDataResponse {

    private final long conid;
    private final long updated;
    private final String lastPrice;
    private final String symbol;
    private final String bidPrice;
    private final String bidSize;
    private final String askPrice;
    private final String askSize;
    private final String dividendDate;
    private final String dividendAmount;
    private final String impliedvolatility;

    public IBMarketDataResponse(
            @JsonProperty("conid") long conid,
            @JsonProperty("_updated") long updated,
            @JsonProperty("31") String lastPrice,
            @JsonProperty("55") String symbol,
            @JsonProperty("84") String bidPrice,
            @JsonProperty("88") String bidSize,
            @JsonProperty("86") String askPrice,
            @JsonProperty("85") String askSize,
            @JsonProperty("7288") String dividendDate,
            @JsonProperty("7286") String dividendAmount,
            @JsonProperty("7633") String impliedvolatility) {
        this.conid = conid;
        this.updated = updated;
        this.lastPrice = lastPrice;
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.bidSize = bidSize;
        this.askPrice = askPrice;
        this.askSize = askSize;
        this.dividendDate = dividendDate;
        this.dividendAmount = dividendAmount;
        this.impliedvolatility = impliedvolatility;
    }

    public long getConid() {
        return conid;
    }

    public long getUpdated() {
        return updated;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public String getBidSize() {
        return bidSize;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public String getAskSize() {
        return askSize;
    }

    public String getDividendDate() {
        return dividendDate;
    }

    public String getDividendAmount() {
        return dividendAmount;
    }

    public String getImpliedvolatility() {
        return impliedvolatility;
    }
}
