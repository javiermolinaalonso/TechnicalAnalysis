package com.assets.options.book.loader.ib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IBSearchResponse {

    private final String conid;
    private final String companyName;
    private final String symbol;
    private final List<IBSearchSections> sections;

    @JsonCreator
    public IBSearchResponse(
            @JsonProperty("conid") String conid,
            @JsonProperty("companyName") String companyName,
            @JsonProperty("symbol") String symbol,
            @JsonProperty("sections") List<IBSearchSections> sections) {
        this.conid = conid;
        this.companyName = companyName;
        this.symbol = symbol;
        this.sections = sections;
    }

    public String getConid() {
        return conid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<IBSearchSections> getSections() {
        return sections;
    }
}
