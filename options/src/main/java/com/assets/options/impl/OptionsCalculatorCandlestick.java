package com.assets.options.impl;

import com.assets.entities.Candlestick;
import com.assets.options.OptionsCalculator;
import com.assets.options.entities.CallOption;
import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.PutOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OptionsCalculatorCandlestick implements OptionsCalculator<Candlestick> {

    private final VolatilityCalculator volatilityCalculator;

    public OptionsCalculatorCandlestick(VolatilityCalculator volatilityCalculator) {
        this.volatilityCalculator = volatilityCalculator;
    }

    @Override
    public CallOption call(List<Candlestick> values, LocalDate expiration, LocalDate now, BigDecimal strike) {
        double volatility = volatilityCalculator.getAnnualizedVolatility(values);
        BigDecimal currentPrice = values.get(values.size() - 1).getFinalPrice();
        return OptionBuilder.create("", currentPrice.doubleValue()).withStrikePrice(strike.doubleValue()).withCurrentDate(now).withExpirationAt(expiration).withIV(volatility).buildCall();
    }

    @Override
    public CallOption call(Candlestick value, LocalDate expiration, LocalDate now, BigDecimal strike, double volatility) {
        BigDecimal currentPrice = value.getFinalPrice();
        return OptionBuilder.create("", currentPrice.doubleValue()).withStrikePrice(strike.doubleValue()).withCurrentDate(now).withExpirationAt(expiration).withIV(volatility).buildCall();
    }

    @Override
    public PutOption put(List<Candlestick> values, LocalDate expiration, LocalDate now, BigDecimal strike) {
        double volatility = volatilityCalculator.getAnnualizedVolatility(values);
        BigDecimal currentPrice = values.get(values.size() - 1).getFinalPrice();
        return OptionBuilder.create("", currentPrice.doubleValue()).withStrikePrice(strike.doubleValue()).withCurrentDate(now).withExpirationAt(expiration).withIV(volatility).buildPut();
    }

    @Override
    public PutOption put(Candlestick value, LocalDate expiration, LocalDate now, BigDecimal strike, double volatility) {
        BigDecimal currentPrice = value.getFinalPrice();
        return OptionBuilder.create("", currentPrice.doubleValue()).withStrikePrice(strike.doubleValue()).withCurrentDate(now).withExpirationAt(expiration).withIV(volatility).buildPut();
    }

}
