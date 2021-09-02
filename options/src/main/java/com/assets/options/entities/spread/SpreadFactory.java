package com.assets.options.entities.spread;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.CallOption;
import com.assets.options.entities.Option;
import com.assets.options.entities.PutOption;
import com.assets.options.entities.spread.exceptions.ExpirationDateMissmatchException;
import com.assets.options.entities.spread.exceptions.OptionTypeMissmatchException;
import com.assets.options.entities.spread.exceptions.TickerMissmatchException;
import com.assets.options.entities.spread.exceptions.VerticalSpreadDifferentStrikePriceException;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import com.assets.options.entities.spread.vertical.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SpreadFactory {

    public BullCallSpread bullCallSpread(CallOption o1, CallOption o2, int contracts) {
        return new BullCallSpread(
                OptionTradeFactory.buy(getLowerStrikeOption(o1, o2), contracts),
                OptionTradeFactory.write(getHigherStrikeOption(o1, o2), contracts)
        );
    }

    public BearCallSpread bearCallSpread(CallOption o1, CallOption o2, int contracts) {
        return new BearCallSpread(
                OptionTradeFactory.write(getLowerStrikeOption(o1, o2), contracts),
                OptionTradeFactory.buy(getHigherStrikeOption(o1, o2), contracts)
        );
    }

    public BullPutSpread bullPutSpread(PutOption o1, PutOption o2, int contracts) {
        return new BullPutSpread(
                OptionTradeFactory.buy(getLowerStrikeOption(o1, o2), contracts),
                OptionTradeFactory.write(getHigherStrikeOption(o1, o2), contracts)
        );
    }

    public BearPutSpread bearPutSpread(PutOption o1, PutOption o2, int contracts) {
        return new BearPutSpread(
                OptionTradeFactory.write(getLowerStrikeOption(o1, o2), contracts),
                OptionTradeFactory.buy(getHigherStrikeOption(o1, o2), contracts)
        );
    }

    public IronCondorSpread ironCondor(PutOption o1, PutOption o2, CallOption o3, CallOption o4, int contracts) {
        return new IronCondorSpread(bearCallSpread(o3, o4, contracts), bullPutSpread(o1, o2, contracts));
    }

    public IronCondorSpread ironCondor(OptionBook book, LocalDate strikeDate, double s1, double s2, double s3, double s4) {
        return ironCondor(
                book.getPutOption(strikeDate, BigDecimal.valueOf(s1)),
                book.getPutOption(strikeDate, BigDecimal.valueOf(s2)),
                book.getCallOption(strikeDate, BigDecimal.valueOf(s3)),
                book.getCallOption(strikeDate, BigDecimal.valueOf(s4)),
                1
        );

    }
    public CalendarCallSpread calendarCallSpread(CallOption closer, CallOption further, int contracts) {
        assert closer.getDaysToExpiry() < further.getDaysToExpiry();
        assert closer.getStrikePrice().equals(further.getStrikePrice());
        return new CalendarCallSpread(
                OptionTradeFactory.write(closer, contracts),
                OptionTradeFactory.buy(further, contracts)
        );
    }

    public VerticalSpread verticalSpread(Option o1, Option o2, int contracts) {
        if (o1.getStrikePrice().equals(o2.getStrikePrice())) {
            throw new VerticalSpreadDifferentStrikePriceException();
        }
        if (!o1.getTicker().equals(o2.getTicker())) {
            throw new TickerMissmatchException();
        }
        if (!o1.getExpirationDate().equals(o2.getExpirationDate())) {
            throw new ExpirationDateMissmatchException();
        }
        if (o1.isCall() != o2.isCall()) {
            throw new OptionTypeMissmatchException();
        }

        if (o1.isCall()) {
            if (o1.compareTo(o2) < 0) {
                return bearCallSpread((CallOption) o1, (CallOption) o2, contracts);
            } else {
                return bullCallSpread((CallOption) o1, (CallOption) o2, contracts);
            }
        } else {
            if (o1.compareTo(o2) < 0) {
                return bullPutSpread((PutOption) o1, (PutOption) o2, contracts);
            } else {
                return bearPutSpread((PutOption) o1, (PutOption) o2, contracts);
            }
        }
    }

    private <T extends Option> T getLowerStrikeOption(T o1, T o2) {
        if (o1.compareTo(o2) < 0) {
            return o1;
        } else {
            return o2;
        }
    }
    private <T extends Option> T getHigherStrikeOption(T o1, T o2) {
        if (o1.compareTo(o2) > 0) {
            return o1;
        } else {
            return o2;
        }
    }
}
