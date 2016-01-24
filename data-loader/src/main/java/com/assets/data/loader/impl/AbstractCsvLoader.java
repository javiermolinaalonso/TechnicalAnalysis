package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCsvLoader implements CsvLoader {

    public List<Candlestick> loadData(String csvFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line;
        List<Candlestick> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            Candlestick candlestick = parseLine(data);
            if (candlestick != null) {
                list.add(candlestick);
            }
        }
        return list;
    }

    protected abstract Candlestick parseLine(String[] data);

}
