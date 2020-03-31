package com.assets.options.main;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOnline;
import com.assets.options.book.loader.yahoo.YahooConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static com.assets.options.main.ObjectMapperProvider.objectMapper;
import static com.assets.options.main.ObjectMapperProvider.restTemplate;

public class YahooDownloader {

    public static void main(String[] args) throws IOException {

        OptionBookLoaderYahooOnline optionBookLoaderYahooOnline = new OptionBookLoaderYahooOnline(restTemplate(), new YahooConverter());

        OptionBook spy = optionBookLoaderYahooOnline.load("SPY");
        writeToFile(spy);
    }

    private static void writeToFile(OptionBook optionBook) throws IOException {
        String path = "/Users/javiermolina/optionsHistory/spy/" + LocalDate.now().toString() + ".json";
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        objectMapper().writeValue(f, optionBook);
    }
}
