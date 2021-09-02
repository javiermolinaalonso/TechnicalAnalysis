package com.assets.data.loader.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YahooDataLoader {

    @Test
    void name() throws IOException {
        ObjectMapper o = new ObjectMapper();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("SPY-2020-10-27.json");
        Map map = o.readValue(stream, Map.class);
        System.out.println(map);
    }
}
