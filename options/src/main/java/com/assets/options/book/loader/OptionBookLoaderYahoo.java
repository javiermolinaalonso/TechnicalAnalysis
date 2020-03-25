package com.assets.options.book.loader;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.YahooConverter;
import com.assets.options.book.loader.yahoo.YahooResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class OptionBookLoaderYahoo {

    private final ObjectMapper objectMapper;
    private final YahooConverter converter;

    public OptionBookLoaderYahoo(ObjectMapper objectMapper, YahooConverter converter) {
        this.objectMapper = objectMapper;
        this.converter = converter;
    }

    public OptionBook loadData(String fileLocation) throws IOException {
        YahooResponse yahooOptionBook = objectMapper.readValue(new File(fileLocation), YahooResponse.class);
        return converter.convert(yahooOptionBook);
    }
}
