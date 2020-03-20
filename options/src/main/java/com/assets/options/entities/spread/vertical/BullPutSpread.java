package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

public class BullPutSpread extends CreditVerticalSpread {

    public BullPutSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

}
