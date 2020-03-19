package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;

public class OptionTradeFactory {

    private static final BigDecimal DEFAULT_COMISSION = BigDecimal.valueOf(1);

    public static OptionTrade write(Option option, int contracts) {
        return new OptionTrade(option, contracts * -1, option.getTicker(), DEFAULT_COMISSION.multiply(BigDecimal.valueOf(contracts)), false);
    }

    public static OptionTrade buy(Option option, int contracts) {
        return new OptionTrade(option, contracts, option.getTicker(), DEFAULT_COMISSION.multiply(BigDecimal.valueOf(contracts)), false);
    }
}
