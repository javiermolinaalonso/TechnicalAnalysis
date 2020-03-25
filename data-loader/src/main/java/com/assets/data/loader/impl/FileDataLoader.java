package com.assets.data.loader.impl;

import java.io.IOException;
import java.util.List;

public interface FileDataLoader<T> {

    List<T> loadData(String fileLocation) throws IOException;

}
