package com.assets.data.loader.impl;

import java.io.IOException;
import java.util.List;

public interface CsvLoader<T> {

    List<T> loadData(String csvFile) throws IOException;

}
