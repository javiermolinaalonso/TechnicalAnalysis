package com.assets.options.main;

import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.OptionBookLoaderCsv;
import com.assets.options.entities.Option;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class GreeksAnalyser {

    public static void main(String[] args) throws IOException {
        LocalDate now = LocalDate.of(2004, 1, 16);
        OptionBookLoaderCsv optionBookLoaderCsv = new OptionBookLoaderCsv(now);
        URL resource = GreeksAnalyser.class.getClassLoader().getResource("options_20040116INTC.csv");
        List<Option> options = optionBookLoaderCsv.loadData(resource.getPath());

        OptionBook optionBook = new OptionBook.Builder()
                .withOptions(options)
                .withCurrentPrice(BigDecimal.valueOf(32.89))
                .withNow(now)
                .withTicker("INTC")
                .build();

        new SpreadAnalyzer().analyzeByDate(optionBook).forEach(r -> System.out.println(r));
    }

}
