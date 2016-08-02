package com.assets.options.optimizers;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.spread.BaseOptionSpread;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDate;

public class PremiumOptimizer {

    public BaseOptionSpread optimize(OptionBook book, LocalDate expiry, LocalDate now) {
        throw new NotImplementedException("TODO");
    }
}
