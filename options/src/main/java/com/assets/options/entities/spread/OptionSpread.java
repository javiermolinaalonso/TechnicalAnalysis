package com.assets.options.entities.spread;

import com.assets.options.entities.Greeks;
import javafx.util.Pair;

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

//    Pair<BigDecimal, BigDecimal> getThresholds();

    double getVolatility();
}
