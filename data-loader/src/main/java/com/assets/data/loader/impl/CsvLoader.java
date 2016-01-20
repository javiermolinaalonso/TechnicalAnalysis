package com.assets.data.loader.impl;

import com.assets.entities.Candlestick;

import java.io.IOException;
import java.util.List;

public interface CsvLoader {

    List<Candlestick> loadData(String csvFile) throws IOException;

}
