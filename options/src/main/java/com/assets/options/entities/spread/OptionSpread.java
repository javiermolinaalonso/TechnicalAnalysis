package com.assets.options.entities.spread;

import com.assets.options.entities.Greeks;

import java.math.BigDecimal;

public interface OptionSpread {

    BigDecimal getMaxGain();

    BigDecimal getMaxLoss();

    BigDecimal getCost();

    BigDecimal getComission();

    Greeks getGreeks();

    BigDecimal getStrikePriceAverage();

    BigDecimal getExpirationValue(BigDecimal bigDecimal);
}
