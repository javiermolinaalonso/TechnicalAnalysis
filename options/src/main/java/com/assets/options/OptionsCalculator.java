package com.assets.options;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.PutOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OptionsCalculator<T> {

    PutOption put(List<T> values, LocalDate expiration, LocalDate now, BigDecimal strike);

    CallOption call(List<T> values, LocalDate expiration, LocalDate now, BigDecimal strike);

    PutOption put(T value, LocalDate expiration, LocalDate now, BigDecimal strike, double volatility);

    CallOption call(T value, LocalDate expiration, LocalDate now, BigDecimal strike, double volatility);
}
