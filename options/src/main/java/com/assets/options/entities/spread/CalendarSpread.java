package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class CalendarSpread extends BaseOptionSpread {

    private final Option closerOption;
    private final Option furtherOption;
    private final BigDecimal strikePrice;

    public CalendarSpread(BigDecimal currentPrice, BigDecimal strikePrice,
                          LocalDate now, LocalDate expirationDate, LocalDate furtherExpirationDate, Double volatility, Double riskFree,
                          BigDecimal comission, String ticker, int contracts, boolean mini) {
        this.strikePrice = strikePrice;
        closerOption = new CallOption(null, currentPrice, strikePrice, now, expirationDate, volatility, riskFree);
        furtherOption = new CallOption(null, currentPrice, strikePrice, now, furtherExpirationDate, volatility, riskFree);
        OptionTrade closerOptionTrade = new OptionTrade(closerOption, contracts * -1, ticker, comission, mini);
        OptionTrade furtherOptionTrade = new OptionTrade(furtherOption, contracts, ticker, comission, mini);
        setOptionTrades(Arrays.asList(closerOptionTrade, furtherOptionTrade));
    }

    @Override
    public BigDecimal getMaxGain() {
        return strikePrice.multiply(BigDecimal.valueOf(2));
    }

    @Override
    public BigDecimal getMaxLoss() {
        return furtherOption.getPremium().negate().subtract(getComission());
    }

    @Override
    public BigDecimal getExpirationValue(BigDecimal value) {
        BigDecimal expectedValue = BigDecimal.ZERO;
        for (OptionTrade optionTrade : getOptionTrades()) {
            BigDecimal tradePremium = optionTrade.getExpirationValue(value);
            expectedValue = expectedValue.add(tradePremium);
        }
        return expectedValue;
    }

}
