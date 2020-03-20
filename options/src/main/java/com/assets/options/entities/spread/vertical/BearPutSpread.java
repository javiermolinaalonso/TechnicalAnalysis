package com.assets.options.entities.spread.vertical;

import com.assets.options.entities.OptionTrade;

public class BearPutSpread extends DebitVerticalSpread {

    public BearPutSpread(OptionTrade lowerOption, OptionTrade upperOption) {
        super(lowerOption, upperOption);
    }

}
