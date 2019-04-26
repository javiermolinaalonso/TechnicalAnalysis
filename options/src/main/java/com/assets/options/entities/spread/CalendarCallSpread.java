package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.StringJoiner;

public class CalendarCallSpread extends BaseOptionSpread {

    private final Option closerOption;
    private final Option furtherOption;
    private final BigDecimal strikePrice;
    private double volatility;

    public static CalendarCallSpread basicSpread(
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
                volatility,
                0.01d,
                BigDecimal.ONE,
                ticker,
                1,
                false,
                volatility
        );
    }

    public static CalendarCallSpread complexSpread(
            double currentPrice,
            double strikePrice,
            LocalDate now,
            int closeDays,
            double closeImpliedVolatility,
            int farDays,
            double farImpliedVolatility,
            String ticker,
            double volatility
    ) {
        return new CalendarCallSpread(
                BigDecimal.valueOf(currentPrice),
                BigDecimal.valueOf(strikePrice),
                now,
                now.plusDays(closeDays),
                now.plusDays(farDays),
                closeImpliedVolatility,
                farImpliedVolatility,
                0.01d,
                BigDecimal.ONE,
                ticker,
                1,
                false,
                volatility
        );
    }
    public CalendarCallSpread(BigDecimal currentPrice, BigDecimal strikePrice,
                              LocalDate now, LocalDate expirationDate, LocalDate furtherExpirationDate, double closeIV, double farIV, Double riskFree,
                              BigDecimal comission, String ticker, int contracts, boolean mini, double volatility) {
        super(mini);
        this.strikePrice = strikePrice;
        this.volatility = volatility;
        closerOption = new CallOption(ticker, currentPrice, strikePrice, now, expirationDate, closeIV, riskFree);
        furtherOption = new CallOption(ticker, currentPrice, strikePrice, now, furtherExpirationDate, farIV, riskFree);
        OptionTrade closerOptionTrade = new OptionTrade(closerOption, contracts * -1, ticker, comission, mini);
        OptionTrade furtherOptionTrade = new OptionTrade(furtherOption, contracts, ticker, comission, mini);
        setOptionTrades(Arrays.asList(closerOptionTrade, furtherOptionTrade));

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

    @Override
    public double getVolatility() {
        return volatility;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ","Calendar {", "}")
                .add(String.format("Strike [%.2f @ %s for %.2f / %.2f @ %s for %.2f]", closerOption.getStrikePrice(), closerOption.getExpirationDate(), closerOption.getPremium(), furtherOption.getStrikePrice(), furtherOption.getExpirationDate(), furtherOption.getPremium()))
                .add(String.format("IV [%.2f%%, %.2f%%]", closerOption.getImpliedVolatility() * 100, furtherOption.getImpliedVolatility() * 100))
                .add(String.format("Cost [%.2f]", getCost()))
                .add(String.format("Max Loss:%.2f, Max Win:%.2f", getMaxLoss(), getMaxGain()))
                .toString();
    }
}
