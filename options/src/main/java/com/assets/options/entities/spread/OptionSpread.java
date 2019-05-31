package com.assets.options.entities.spread;

import com.assets.options.entities.Greeks;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OptionSpread {

    BigDecimal getMaxGain();

    BigDecimal getMaxLoss();

    BigDecimal getCost();

    BigDecimal getCost(BigDecimal currentPrice, LocalDate when, double volatility);

    BigDecimal getComission();

    Greeks getGreeks();

    BigDecimal getStrikePriceAverage();

    BigDecimal getExpirationValue(BigDecimal bigDecimal);

    BigDecimal getValueAt(BigDecimal value, LocalDate when);

    LocalDate getExpirationDate();

    double getVolatility();

}
