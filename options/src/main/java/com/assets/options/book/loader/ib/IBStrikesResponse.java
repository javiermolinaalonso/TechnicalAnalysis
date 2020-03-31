package com.assets.options.book.loader.ib;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IBStrikesResponse {

    private final List<String> calls;
    private final List<String> puts;

    @JsonCreator
    public IBStrikesResponse(
            @JsonProperty("call") List<String> calls,
            @JsonProperty("put") List<String> puts
    ) {
        this.calls = calls;
        this.puts = puts;
    }

    public List<String> getCalls() {
        return calls;
    }

    public List<String> getPuts() {
        return puts;
    }
}
