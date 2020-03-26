package com.assets.options.main;

import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.book.OptionBook;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static com.assets.options.main.ObjectMapperProvider.objectMapper;

public class OptionBookAnalyzer {

    public static void main(String[] args) throws IOException {
        String path = "/Users/javiermolina/optionsHistory/spy/" + LocalDate.now().toString() + ".json";
        OptionBook optionBook = objectMapper().readValue(new File(path), OptionBook.class);
        new SpreadAnalyzer().analyzeByDate(optionBook).forEach(r -> System.out.println(r));
    }


}
