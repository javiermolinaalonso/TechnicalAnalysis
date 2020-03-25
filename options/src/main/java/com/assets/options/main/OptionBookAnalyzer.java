package com.assets.options.main;

import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.book.OptionBook;

import java.io.File;
import java.io.IOException;

import static com.assets.options.main.ObjectMapperProvider.objectMapper;

public class OptionBookAnalyzer {

    public static void main(String[] args) throws IOException {
        OptionBook optionBook = objectMapper().readValue(new File("/Users/javiermolina/Downloads/SPY-2020-03-25.json-2.txt"), OptionBook.class);
        new SpreadAnalyzer().analyzeByDate(optionBook).forEach(r -> System.out.println(r));
    }


}
