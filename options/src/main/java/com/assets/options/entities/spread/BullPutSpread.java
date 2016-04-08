package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.PutOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class BullPutSpread extends OptionSpread {

    public BullPutSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                         BigDecimal lowStrikePremium, BigDecimal highStrikePremium,
                         LocalDate now, LocalDate expirationDate, Double riskFree,
                         BigDecimal comission, String ticker, int contracts, boolean mini) {
        Option lowerOption = new PutOption(currentPrice, lowStrikePrice, lowStrikePremium, now, expirationDate, riskFree);
        Option upperOption = new PutOption(currentPrice, highStrikePrice, highStrikePremium, now, expirationDate, riskFree);
        doSpread(comission, ticker, contracts, mini, lowerOption, upperOption);
    }

    public BullPutSpread(BigDecimal currentPrice, BigDecimal lowStrikePrice, BigDecimal highStrikePrice,
                          LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                          BigDecimal comission, String ticker, int contracts, boolean mini) {
        Option lowerOption = new PutOption(currentPrice, lowStrikePrice, now, expirationDate, volatility, riskFree);
        Option upperOption = new PutOption(currentPrice, highStrikePrice, now, expirationDate, volatility, riskFree);
        doSpread(comission, ticker, contracts, mini, lowerOption, upperOption);
    }

    private void doSpread(BigDecimal comission, String ticker, int contracts, boolean mini, Option lowerOption, Option upperOption) {
        OptionTrade lowerOptionTrade = new OptionTrade(lowerOption, contracts, ticker, comission, mini);
        OptionTrade upperOptionTrade = new OptionTrade(upperOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerOptionTrade, upperOptionTrade));
    }

}
