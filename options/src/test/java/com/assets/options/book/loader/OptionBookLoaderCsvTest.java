package com.assets.options.book.loader;

import com.assets.options.entities.Option;
import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OptionBookLoaderCsvTest {

    private LocalDate now = LocalDate.of(2004, 1, 16);

    @Test
    public void testOptionBookLoader() throws Exception {

        URL resource = OptionBookLoaderCsvTest.class.getClassLoader().getResource("options_20040116.csv");
        assert resource != null;
        OptionBookLoaderCsv bookLoader = new OptionBookLoaderCsv(now);

        List<Option> options = bookLoader.loadData(resource.getPath());
        assertEquals(138319, options.size());
    }
}