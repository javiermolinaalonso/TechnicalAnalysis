package com.assets.statistics.correlation;

import com.assets.data.loader.yahoo.YahooDataLoader;
import com.assets.statistic.list.StockList;
import com.assets.statistics.correlation.impl.CorrelationServiceImpl;
import com.assets.statistics.entities.CorrelationIntervalInputData;
import com.assets.statistics.entities.CorrelationTwoStocks;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCorrelation {

    private LocalDateTime from = LocalDateTime.of(2021, 1, 1,0, 0);
    private LocalDateTime to = LocalDateTime.of(2022, 1, 1, 0, 0);

//    private String[] tickers = {"AAPL", "MSFT", "AMZN", "TSLA", "GOOGL", "NVDA", "BRKB", "FB", "UNH", "JNJ", "JPM", "V", "PG", "XOM", "HD", "CVX", "MA", "BAC", "ABBV", "PFE", "AVGO", "COST", "DIS", "KO"};
    private String[] tickers = {"%5EVIX"};
    YahooDataLoader loader = new YahooDataLoader(from, to);

    private CorrelationService correlationService = new CorrelationServiceImpl();

    @Test
    void name() {
        Map<String, StockList> map = new HashMap<>();
        for (String ticker : tickers) {
            map.put(ticker, loader.loadData(ticker));
        }

        CorrelationTwoStocks inputData = new CorrelationTwoStocks(map.get("AAPL"), map.get("MSFT"), from, to);
        correlationService.computeCorrelationBetweenTwoStocks(inputData, List.of(new CorrelationIntervalInputData(1, ChronoUnit.YEARS, 1, ChronoUnit.DAYS)));
    }
}
