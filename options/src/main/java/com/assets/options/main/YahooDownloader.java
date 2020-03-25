package com.assets.options.main;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOnline;
import com.assets.options.book.loader.yahoo.YahooConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static com.assets.options.main.ObjectMapperProvider.objectMapper;

public class YahooDownloader {

    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        final MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper());
        restTemplate.setMessageConverters(Arrays.asList(jacksonConverter));

        OptionBookLoaderYahooOnline optionBookLoaderYahooOnline = new OptionBookLoaderYahooOnline(restTemplate, new YahooConverter());

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
