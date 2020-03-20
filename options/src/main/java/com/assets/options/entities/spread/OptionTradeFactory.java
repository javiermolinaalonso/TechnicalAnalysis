package com.assets.options.entities.spread;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;

public class OptionTradeFactory {

    private static final double DEFAULT_FEE = 1d;

    public static OptionTrade write(Option option, int contracts) {
        return write(option, contracts, DEFAULT_FEE);
    }
    public static OptionTrade write(Option option, int contracts, double fee) {
        assert contracts > 0;
        return new OptionTrade(option, contracts * -1, option.getTicker(), BigDecimal.valueOf(fee), false);
    }

    public static OptionTrade buy(Option option, int contracts) {
        return buy(option, contracts, DEFAULT_FEE);
    }

    public static OptionTrade buy(Option option, int contracts, double fee) {
        assert contracts > 0;
        return new OptionTrade(option, contracts, option.getTicker(), BigDecimal.valueOf(fee), false);
    }
}
