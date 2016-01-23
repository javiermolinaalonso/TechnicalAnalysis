package com.assets.options;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OptionsCalculator<T> {

    double put(List<T> values, LocalDate expiration, double strike);

    double call(List<T> values, LocalDate expiration, double strike);

    double put(T value, LocalDate expiration, double strike, double volatility);

    double call(T value, LocalDate expiration, double strike, double volatility);
}
