package com.assets.options.entities.spread;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.PutOption;
import com.assets.options.entities.spread.vertical.BearSpread;
import com.assets.options.entities.spread.vertical.BearPutSpread;
import com.assets.options.entities.spread.vertical.BullSpread;
import com.assets.options.entities.spread.vertical.BullPutSpread;

public class SpreadFactory {

    public BullSpread bullCallSpread(CallOption o1, CallOption o2, int contracts) {
        assert o1.compareTo(o2) < 0;
        return new BullSpread(
                OptionTradeFactory.buy(o1, contracts),
                OptionTradeFactory.write(o2, contracts)
        );
    }

    public BearSpread bearCallSpread(CallOption o1, CallOption o2, int contracts) {
        assert o1.compareTo(o2) < 0;
        return new BearSpread(
                OptionTradeFactory.write(o1, contracts),
                OptionTradeFactory.buy(o2, contracts)
        );
    }

    public BullPutSpread bullPutSpread(PutOption o1, PutOption o2, int contracts) {
        assert o1.compareTo(o2) < 0;
        return new BullPutSpread(
                OptionTradeFactory.buy(o1, contracts),
                OptionTradeFactory.write(o2, contracts)
        );
    }

    public BearPutSpread bearPutSpread(PutOption o1, PutOption o2, int contracts) {
        assert o1.compareTo(o2) < 0;
        return new BearPutSpread(
                OptionTradeFactory.write(o1, contracts),
                OptionTradeFactory.buy(o2, contracts)
        );
    }

    public IronCondorSpread ironCondor(PutOption o1, PutOption o2, CallOption o3, CallOption o4, int contracts) {
        return new IronCondorSpread(bearCallSpread(o3, o4, contracts), bullPutSpread(o1, o2, contracts));
    }
}
