package com.assets.options.book.loader;

import com.assets.options.entities.Option;
import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class OptionBookLoaderCsvTest {

    private LocalDate now = LocalDate.of(2004, 1, 16);

    @Test
    public void testOptionBookLoader() throws Exception {

        URL resource = OptionBookLoaderCsvTest.class.getClassLoader().getResource("options_20040116.csv");
        assert resource != null;
        OptionBookLoaderCsv bookLoader = new OptionBookLoaderCsv(now);

        List<Option> options = bookLoader.loadData(resource.getPath());

        for (Option option : options) {
            System.out.println(String.format("%s,%.2f,%.2f",option.getTicker(), option.getCurrentPrice().doubleValue(), option.getStrikePrice().doubleValue()));
        }
    }
}