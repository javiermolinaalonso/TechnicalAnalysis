package com.assets.options.book.loader.yahoo;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class YahooConverter {
    
    public OptionBook convert(YahooResponse data) {
        String ticker = data.getTicker();
        double currentPrice = data.getCurrentPrice();
        LocalDate now = data.getCurrentDate();

        List<Option> options = new ArrayList<>();
        for (YahooOption option : data.getOptionChain()) {
            LocalDate expiration = Instant.ofEpochSecond(option.getExpiry()).atZone(ZoneId.of("UTC")).toLocalDate();
            OptionBuilder optionBuilder = OptionBuilder.create(ticker, currentPrice)
                    .withStrikePrice(option.getStrike())
                    .withBidAsk(option.getBid(), option.getAsk())
                    .withCurrentDate(now)
                    .withExpirationAt(expiration);
            if(option.isCall()) {
                options.add(optionBuilder.buildCall());
            } else {
                options.add(optionBuilder.buildPut());
            }
        }

        return new OptionBook.Builder()
                .withOptions(options)
                .withCurrentPrice(BigDecimal.valueOf(currentPrice))
                .withNow(now)
                .withTicker(ticker)
                .build();
    }
}
