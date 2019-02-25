package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class CalendarCallSpread extends BaseOptionSpread {

    private final Option closerOption;
    private final Option furtherOption;
    private final BigDecimal strikePrice;

    public static final CalendarCallSpread basicSpread(
            double currentPrice,
            double strikePrice,
            int closeDays,
            int farDays,
            double volatility,
            String ticker
    ) {
        return new CalendarCallSpread(
                BigDecimal.valueOf(currentPrice),
                BigDecimal.valueOf(strikePrice),
                LocalDate.now(),
                LocalDate.now().plusDays(closeDays),
                LocalDate.now().plusDays(farDays),
                volatility,
                0.01d,
                BigDecimal.ONE,
                ticker,
                1,
                false
        );
    }
    public CalendarCallSpread(BigDecimal currentPrice, BigDecimal strikePrice,
                              LocalDate now, LocalDate expirationDate, LocalDate furtherExpirationDate, Double volatility, Double riskFree,
                              BigDecimal comission, String ticker, int contracts, boolean mini) {
        this.strikePrice = strikePrice;
        closerOption = new CallOption(ticker, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
        furtherOption = new CallOption(ticker, currentPrice, strikePrice, now, furtherExpirationDate, volatility, riskFree);
        OptionTrade closerOptionTrade = new OptionTrade(closerOption, contracts * -1, ticker, comission, mini);
        OptionTrade furtherOptionTrade = new OptionTrade(furtherOption, contracts, ticker, comission, mini);
        setOptionTrades(Arrays.asList(closerOptionTrade, furtherOptionTrade));
        this.mini = mini;
    }

    @Override
    public BigDecimal getMaxGain() {
        return getExpirationValue(strikePrice).subtract(getComission());
    }

    @Override
    public BigDecimal getMaxLoss() {
        return furtherOption.getPremium().subtract(closerOption.getPremium())
                .multiply(getMultiplier())
                .add(getComission())
                .negate();
    }

    @Override
    public BigDecimal getExpirationValue(BigDecimal value) {
        return getValueAt(value, closerOption.getExpirationDate());
    }

    @Override
    public LocalDate getExpirationDate() {
        return closerOption.getExpirationDate();
    }

}
