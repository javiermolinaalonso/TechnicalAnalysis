package com.assets.options.book.loader;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOffline;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.options.entities.spread.ShortStraddleSpread;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.assets.options.OptionUtils.getThirdFridayOfMonth;
import static org.hamcrest.MatcherAssert.assertThat;

public class OptionDailySellStrangleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(OptionDailySellStrangleStrategy.class);

    LocalDate from = LocalDate.of(2021, 1, 1);
    LocalDate to = LocalDate.of(2021, 1, 31);
    LocalDate now = from;
    LocalDate targetExpirationDate = LocalDate.of(2021, 2, 19);
    private String symbol = "SPY";
    private String folder = "/Users/javiermolina/optionsHistory";

    @Test
    void sellStraddlesStrategy() {
        List<OptionSpread> spreads = getOptionSpreads(o -> getShortStraddleSpread(targetExpirationDate, o, o.getCurrentPrice()));

        OptionBook optionBookAtExpiry = OptionBookLoaderYahooOffline.load(folder, targetExpirationDate, symbol).get();
        logger.info("Strike, Cost, Value, Net Profit, Profit");
        spreads.forEach(s -> {
            OptionSpread spreadAtExpiry = getShortStraddleSpread(targetExpirationDate, optionBookAtExpiry, s.getStrikePriceAverage());
            printResults(s, spreadAtExpiry);
        });
        logger.info("Margin required: {}", spreads.stream().mapToDouble(s -> s.getMargin().doubleValue()).sum());
        logger.info("Price at expiry: {}", optionBookAtExpiry.getCurrentPrice());

    }

    @Test
    void sellIronCondorStrategy() {
        List<OptionSpread> spreads = getOptionSpreads(o -> getIronCondorSpread(targetExpirationDate, o, o.getCurrentPrice()));

        OptionBook optionBookAtExpiry = OptionBookLoaderYahooOffline.load(folder, targetExpirationDate, symbol).get();
        logger.info("Strike, Cost, Value, Net Profit, Profit");
        spreads.forEach(s -> {
            OptionSpread spreadAtExpiry = getIronCondorSpread(targetExpirationDate, optionBookAtExpiry, s.getStrikePriceAverage());
            printResults(s, spreadAtExpiry);
        });
        logger.info("Margin required: {}", spreads.stream().mapToDouble(s -> s.getMargin().doubleValue()).sum());
        logger.info("Price at expiry: {}", optionBookAtExpiry.getCurrentPrice());
    }

    @Test
    void sellIronCondorStrategyForever() {
        logger.info("Strike, Cost, Value, Net Profit, Profit");
        LocalDate from = LocalDate.of(2021, 4, 1);
        LocalDate to = LocalDate.of(2021, 4, 2);
        LocalDate now = from;

        while (now.isBefore(to)) {
            Month nextMonth = now.plusMonths(1).getMonth();
            LocalDate expirationDate = getThirdFridayOfMonth(now.plusMonths(1).getYear(), nextMonth);
            List<OptionSpread> thisMonthSpreads = new ArrayList<>();
            try {
                OptionBookLoaderYahooOffline.load(folder, now, symbol).ifPresent(o -> {
                    thisMonthSpreads.add(getIronCondorSpread(expirationDate, o, o.getCurrentPrice()));
                });
            } catch (Exception ignored) {
            }
            OptionBook optionBookAtExpiry = OptionBookLoaderYahooOffline.load(folder, expirationDate, symbol).get();
            thisMonthSpreads.forEach(s -> {
                OptionSpread spreadAtExpiry = getIronCondorSpread(expirationDate, optionBookAtExpiry, s.getStrikePriceAverage());
                printResults(s, spreadAtExpiry);
            });
//            logger.info("Margin required: {}", thisMonthSpreads.stream().mapToDouble(s -> s.getMargin().doubleValue()).sum());
            now = now.plusDays(1);
        }

    }

    @Test
    void sellIronCondorStrategyForeverMeh() {
        logger.info("Delta, Gamma, Theta, Vega, Rho, premium, price");
        LocalDate from = LocalDate.of(2021, 4, 1);
        LocalDate to = LocalDate.of(2021, 4, 2);
        LocalDate now = from;

        while (now.isBefore(to)) {
            Month nextMonth = now.plusMonths(1).getMonth();
            LocalDate expirationDate = getThirdFridayOfMonth(now.plusMonths(1).getYear(), nextMonth);
            List<OptionSpread> thisMonthSpreads = new ArrayList<>();
            try {
                OptionBookLoaderYahooOffline.load(folder, now, symbol).ifPresent(o -> {
                    thisMonthSpreads.add(getIronCondorSpread(expirationDate, o, o.getCurrentPrice()));
                });
            } catch (Exception ignored) {
            }
            OptionBook optionBookAtExpiry = OptionBookLoaderYahooOffline.load(folder, expirationDate, symbol).get();
            for (OptionSpread s : thisMonthSpreads) {
                OptionSpread spreadAtExpiry = getIronCondorSpread(expirationDate, optionBookAtExpiry, s.getStrikePriceAverage());
//                printResults(s, spreadAtExpiry);
                LocalDate f = LocalDate.from(now);
                while (f.isBefore(expirationDate)) {
                    OptionBookLoaderYahooOffline.load(folder, f, symbol).ifPresent(o -> {
                        IronCondorSpread ironCondorSpread = getIronCondorSpread(expirationDate, o, s.getStrikePriceAverage());
                        logger.info(ironCondorSpread.getGreeks().toString() + ", " + ironCondorSpread.getCost() + ", " + o.getCurrentPrice().setScale(2, RoundingMode.HALF_DOWN));
                    });
                    f = f.plusDays(1);
                }
            }
//            logger.info("Margin required: {}", thisMonthSpreads.stream().mapToDouble(s -> s.getMargin().doubleValue()).sum());
            now = now.plusDays(1);
        }

    }

    @NotNull
    private List<OptionSpread> getOptionSpreads(Function<OptionBook, ? extends OptionSpread> getSpreadFunction) {
        List<OptionSpread> spreads = new ArrayList<>();
        do {
//            if (now.getDayOfWeek() == DayOfWeek.MONDAY) {
            try {
                OptionBookLoaderYahooOffline.load(folder, now, symbol).map(getSpreadFunction).ifPresent(spreads::add);
            } catch (Exception e) {
                logger.warn("Option for day {} not found", now);
            }
//            }
            now = now.plusDays(1);
        } while (now.isBefore(to));
        return spreads;
    }

    private OptionSpread getShortStraddleSpread(LocalDate targetExpirationDate, OptionBook optionBook, BigDecimal currentPrice) {
        BigDecimal callStrike = currentPrice.setScale(0, RoundingMode.UP);
        BigDecimal putStrike = currentPrice.setScale(0, RoundingMode.DOWN);
        return new ShortStraddleSpread(optionBook.getCallOption(targetExpirationDate, callStrike), optionBook.getPutOption(targetExpirationDate, putStrike));
    }

    private IronCondorSpread getIronCondorSpread(LocalDate targetExpirationDate, OptionBook o, BigDecimal currentPrice) {
        BigDecimal purchasePutStrike = currentPrice.multiply(BigDecimal.valueOf(0.95)).setScale(0, RoundingMode.DOWN);
        BigDecimal sellPutStrike = currentPrice.multiply(BigDecimal.valueOf(0.98)).setScale(0, RoundingMode.DOWN);
        BigDecimal sellCallStrike = currentPrice.multiply(BigDecimal.valueOf(1.02)).setScale(0, RoundingMode.UP);
        BigDecimal buyCallStrike = currentPrice.multiply(BigDecimal.valueOf(1.05)).setScale(0, RoundingMode.UP);

        return new SpreadFactory().ironCondor(
                o.getPutOption(targetExpirationDate, purchasePutStrike),
                o.getPutOption(targetExpirationDate, sellPutStrike),
                o.getCallOption(targetExpirationDate, sellCallStrike),
                o.getCallOption(targetExpirationDate, buyCallStrike),
                1
        );
    }

    private void printResults(OptionSpread s, OptionSpread sExpiry) {
        BigDecimal profit = s.getCost().negate().add(sExpiry.getCost());
        logger.info(String.format("%s, %s, %s, %s, %s%%, %.4f",
                s.getStrikePriceAverage(),
                s.getCost(),
                sExpiry.getCost(),
                profit,
                profit.divide(s.getMargin(), 4, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN),
                s.getVolatility() * 100d
        ));
    }


    @Test
    void verifyFridays() {
        assertThat(getThirdFridayOfMonth(2021, Month.AUGUST), Matchers.equalTo(LocalDate.of(2021, 8, 20)));
        assertThat(getThirdFridayOfMonth(2021, Month.JULY), Matchers.equalTo(LocalDate.of(2021, 7, 16)));
        assertThat(getThirdFridayOfMonth(2021, Month.MAY), Matchers.equalTo(LocalDate.of(2021, 5, 21)));
        assertThat(getThirdFridayOfMonth(2021, Month.FEBRUARY), Matchers.equalTo(LocalDate.of(2021, 2, 19)));
        assertThat(getThirdFridayOfMonth(2021, Month.JANUARY), Matchers.equalTo(LocalDate.of(2021, 1, 15)));
    }
}
