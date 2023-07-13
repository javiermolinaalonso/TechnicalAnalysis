package com.assets.options.book.loader.yahoo;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Greeks;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OptionBookLoaderYahooOffline {

    private static final String DEFAULT_SYMBOL = "SPY";
    private static final String DEFAULT_FOLDER = "/Users/javi/options/";

    private static final String format = "%s-%s-%s-%s.json";

    public static Optional<OptionBook> load(LocalDate when) {
        return load(DEFAULT_FOLDER, when, DEFAULT_SYMBOL);
    }

    public static Optional<OptionBook> load(LocalDate when, String symbol) {
        return load(DEFAULT_FOLDER, when, symbol);
    }

    public static Optional<OptionBook> load(String folder, LocalDate when, String symbol) {
        String file = folder + String.format(format, symbol, when.getYear(), String.format("%02d", when.getMonthValue()), String.format("%02d", when.getDayOfMonth()));
        try {
            return Optional.of(load(new FileInputStream(new File(file))));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static OptionBook load(InputStream is) throws IOException {
        Map map = new ObjectMapper().readValue(is, Map.class);
        Double price = (Double) map.get("currentPrice");
        String ticker = (String) map.get("ticker");

        List<Option> options = getOptions((List<Map<String, Object>>) map.get("options"));
        return OptionBook.Builder.create()
                .withCurrentPrice(BigDecimal.valueOf(price))
                .withNow(extractDate(map, "now"))
                .withTicker(ticker)
                .withOptions(options)
                .build();
    }

    private static LocalDate extractDate(Map map, String key) {
        List<Integer> now = (List<Integer>) map.get(key);
        return LocalDate.of(now.get(0), now.get(1), now.get(2));
    }

    private static List<Option> getOptions(List<Map<String, Object>> options) {
        return options.stream().map(o -> {
            boolean isCall = (boolean) o.get("call");
            String ticker = (String) o.get("ticker");
            double currentPrice = (double) o.get("currentPrice");
            double strikePrice = (double) o.get("strikePrice");
            double bid = (double) o.get("bid");
            double ask = (double) o.get("ask");
            LocalDate currentDate = extractDate(o, "currentDate");
            LocalDate expirationDate = extractDate(o, "expirationDate");
            double impliedVolatility = (double) o.get("impliedVolatility");
            double riskFree = (double) o.get("riskFree");

            OptionBuilder optionBuilder = OptionBuilder.create(ticker, currentPrice)
                    .withBidAsk(bid, ask)
                    .withIV(impliedVolatility)
                    .withCurrentDate(currentDate)
                    .withExpirationAt(expirationDate)
                    .withStrikePrice(strikePrice)
                    .withRiskFree(riskFree);
            if (isCall) {
                return optionBuilder.buildCall();
            } else {
                return optionBuilder.buildPut();
            }
        }).collect(Collectors.toList());
    }

    private Greeks getGreeks(Map<String, Object> greeks) {
        double delta = (double) greeks.get("delta");
        double gamma = (double) greeks.get("gamma");
        double vega = (double) greeks.get("vega");
        double theta = (double) greeks.get("theta");
        double rho = (double) greeks.get("rho");
        return new Greeks(delta, gamma, vega, theta, rho);
    }
}
