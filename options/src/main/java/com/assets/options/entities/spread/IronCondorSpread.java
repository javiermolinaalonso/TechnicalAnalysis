package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.PutOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class IronCondorSpread extends OptionSpread {

    public IronCondorSpread(BigDecimal currentPrice,
                            BigDecimal lowLossStrike, BigDecimal lowProfitStrike,
                            BigDecimal highProfitStrike, BigDecimal highStrikeLossPrice,
                            LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree,
                            BigDecimal comission, String ticker, int contracts, boolean mini) {
        Option lowerPutOption = new PutOption(currentPrice, lowLossStrike, now, expirationDate, volatility, riskFree);
        Option upperPutOption = new PutOption(currentPrice, lowProfitStrike, now, expirationDate, volatility, riskFree);
        Option lowerCallOption = new CallOption(currentPrice, highProfitStrike, now, expirationDate, volatility, riskFree);
        Option upperCallOption = new CallOption(currentPrice, highStrikeLossPrice, now, expirationDate, volatility, riskFree);
        OptionTrade lowerPutTrade = new OptionTrade(lowerCallOption, contracts, ticker, comission, mini);
        OptionTrade upperPutTrade = new OptionTrade(upperCallOption, contracts * -1, ticker, comission, mini);
        OptionTrade lowerCallTrade = new OptionTrade(lowerCallOption, contracts * -1, ticker, comission, mini);
        OptionTrade upperCallTrade = new OptionTrade(upperCallOption, contracts, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerPutTrade, upperPutTrade, lowerCallTrade, upperCallTrade));

    }
}
