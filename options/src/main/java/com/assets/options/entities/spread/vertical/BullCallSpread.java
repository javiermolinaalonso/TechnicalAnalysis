package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

public class BullCallSpread extends DebitVerticalSpread {

    public BullCallSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

}
