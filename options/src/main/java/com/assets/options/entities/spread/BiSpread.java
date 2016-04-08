package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.util.Arrays;

public abstract class BiSpread extends OptionSpread {

    public BiSpread(Option lowerOption, Option upperOption, BigDecimal comission, String ticker, int contracts, boolean mini) {
        OptionTrade lowerOptionTrade = new OptionTrade(lowerOption, contracts, ticker, comission, mini);
        OptionTrade upperOptionTrade = new OptionTrade(upperOption, contracts * -1, ticker, comission, mini);
        setOptionTrades(Arrays.asList(lowerOptionTrade, upperOptionTrade));
    }
}
