package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

public class BearCallSpread extends CreditVerticalSpread {

    public BearCallSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }


}
