package com.assets.data.loader.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCsvLoader<T> implements FileDataLoader<T> {

    public List<T> loadData(String fileLocation) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileLocation));
        String line;
        List<T> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            T value = parseLine(data);
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    protected abstract T parseLine(String[] data);

}
