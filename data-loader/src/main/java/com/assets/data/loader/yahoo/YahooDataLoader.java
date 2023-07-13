package com.assets.data.loader.yahoo;

import com.assets.data.loader.DataLoader;
import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YahooDataLoader implements DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(YahooDataLoader.class);

    private static final String BASE_URL = "https://yfapi.net/v8/finance/chart/";
//    https://yfapi.net/v8/finance/chart/AAPL?comparisons=MSFT%2C%5EVIX&range=1mo&region=US&interval=1d&lang=en&events=div%2Csplit

    private final LocalDateTime to;
    private final LocalDateTime from;

    public YahooDataLoader(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    private String getMillis(LocalDateTime to) {
        return String.valueOf(to.toEpochSecond(ZoneOffset.UTC));
    }

    @Override
    public StockList loadData(String ticker) {
        System.out.println(String.format("Downloading data for %s", ticker));
        final String url = buildUrl(ticker);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-api-key", "JyB3wDAO8c36KBvUMk1HY3ybO184IKe18sLuTcp2");
        final YahooData data = new RestTemplate()
                .getForObject(url, YahooData.class, headers);
        final YahooChartElement result = data.getChart().getResult().get(0);

        List<StockPrice> prices = new ArrayList<>();
        final List<Integer> timestamp = result.getTimestamp();
        final List<Double> adjclose = result.getIndicators().getQuote().get(0).get("close");
        for (int i = 0; i < timestamp.size(); i++) {
            try {
                prices.add(new StockPrice(ticker, normalizeInstant(timestamp.get(i)), BigDecimal.valueOf(adjclose.get(i))));
            } catch (NullPointerException e) {
                logger.warn("Ticker {} at position {} has null value", ticker, i);
            }
        }

        return new StockList(prices, ticker);
    }

    private String buildUrl(String ticker) {
        return String.format("%s%s?period2=%s&period1=%s&interval=%s&indicators=quote&includeTimestamps=true&includePrePost=true", BASE_URL, ticker, getMillis(to), getMillis(from), YahooDataPeriod.ONE_DAY.getYahoovalue());
    }

    private Instant normalizeInstant(Integer second) {
        final Instant instant = Instant.ofEpochSecond(second);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant(ZoneOffset.UTC);
    }

}
