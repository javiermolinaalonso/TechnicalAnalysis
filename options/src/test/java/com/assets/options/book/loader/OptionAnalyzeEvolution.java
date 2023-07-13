package com.assets.options.book.loader;

import com.assets.options.OptionUtils;
import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOffline;
import com.assets.options.entities.CallOption;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionAnalyzeEvolution {

    private static final Logger logger = LogManager.getLogger(OptionAnalyzeEvolution.class);

    private final LocalDate from = LocalDate.of(2021, 1, 1);
    private final LocalDate targetExpirationDate = OptionUtils.getThirdFridayOfMonth(2021, Month.MARCH);
    private final BigDecimal profitRatio = BigDecimal.valueOf(1.02);
    private final double minDelta = 30;

    @Test
    void foo() throws IOException {
        OptionPortfolio portfolio = new OptionPortfolio(new ArrayList<>());
        for (LocalDate date = from; date.isBefore(targetExpirationDate); date = date.plusDays(1)) {
            Optional<OptionBook> optBook = OptionBookLoaderYahooOffline.load(date);
            if (optBook.isPresent()) {
                OptionBook book = optBook.get();
                BigDecimal currentPrice = book.getCurrentPrice();
                double currentDelta = getDelta(portfolio, book);
                if (currentDelta > minDelta) {
                    BigDecimal strike = currentPrice.multiply(profitRatio).setScale(0, RoundingMode.HALF_DOWN);
                    CallOption callOption = book.getCallOption(targetExpirationDate, strike);
                    portfolio.add(50);
                    portfolio.add(List.of(new OptionTrade(callOption, -1, "SPY", BigDecimal.ZERO, false)));
                    logger.info("Sold call {}. Purchased shares at {}", callOption, currentPrice);
                }
                logger.info("Current price {}. Current Delta {}", currentPrice, currentDelta);
            }
        }
        logger.info("yay");
    }

    private double getDelta(OptionPortfolio portfolio, OptionBook book) {
        Integer deltas = portfolio.getShares();
        if (deltas < 1) {
            return 100;
        }
        double delta = portfolio.getTrades().stream()
                .map(OptionTrade::getOption)
                .flatMap(o -> book.getOption(o.getExpirationDate(), o.getStrikePrice(), o.isCall()).stream())
                .mapToDouble(k -> k.getGreeks().getDelta() * -1)
                .sum() * 100;

        return deltas + delta;
    }

    private int getIndexOf(BarSeries series, LocalDate date) {
        for (int i = 0; i < series.getBarCount(); i++) {
            if (series.getBar(i).getEndTime().toLocalDate().isEqual(date)) {
                return i;
            }
        }
        return -1;
    }
}
