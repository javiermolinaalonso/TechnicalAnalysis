package com.assets.options.book.loader.ib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IBSearchSections {

    private final String secType;
    private final String months;
    private final String symbol;

    @JsonCreator
    public IBSearchSections(
            @JsonProperty("secType") String secType,
            @JsonProperty("months") String months,
            @JsonProperty("symbol") String symbol) {
        this.secType = secType;
        this.months = months;
        this.symbol = symbol;
    }

    public String getSecType() {
        return secType;
    }

    public String getMonths() {
        return months;
    }

    public String getSymbol() {
        return symbol;
    }
}
