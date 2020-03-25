package com.assets.options.book.loader.yahoo;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Option;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.time.DayOfWeek.FRIDAY;

public class OptionBookLoaderYahooOnline {

    private static final String path = "https://query2.finance.yahoo.com/v7/finance/options/";

    private final RestTemplate restTemplate;
    private final YahooConverter yahooConverter;

    public OptionBookLoaderYahooOnline(RestTemplate restTemplate, YahooConverter yahooConverter) {
        this.restTemplate = restTemplate;
        this.yahooConverter = yahooConverter;
    }

    public OptionBook load(String symbol) {
        String uri = path + symbol;
        YahooResponse response = restTemplate.getForObject(uri, YahooResponse.class);

        List<Option> options = new ArrayList<>();
        for(Long date : response.getExpirationDates()) {
            LocalDate expiration = Instant.ofEpochSecond(date).atZone(ZoneId.of("UTC")).toLocalDate();

            if (expiration.getDayOfWeek() == FRIDAY && expiration.getDayOfMonth() > 14 && expiration.getDayOfMonth() < 22) {
                String dateUrl = String.format("%s%s?date=%s", OptionBookLoaderYahooOnline.path, symbol, date.toString());
                YahooResponse forDate = restTemplate.getForObject(dateUrl, YahooResponse.class);
                options.addAll(yahooConverter.convert(forDate).getOptions());
            }
        }

        return new OptionBook.Builder()
                .withCurrentPrice(BigDecimal.valueOf(response.getCurrentPrice()))
                .withNow(response.getCurrentDate())
                .withTicker(response.getTicker())
                .withOptions(options)
                .build();
    }
}
