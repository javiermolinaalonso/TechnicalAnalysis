package com.assets.options.book;

import com.assets.options.entities.Option;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonDeserialize(builder = OptionBook.Builder.class)
public class OptionBook {

    private final String ticker;
    private final BigDecimal currentPrice;
    private final LocalDate now;
    private final Map<LocalDate, List<Option>> options;

    private OptionBook(Builder builder) {
        ticker = builder.ticker;
        currentPrice = builder.currentPrice;
        now = builder.now;
        options = builder.options.stream().collect(Collectors.groupingBy(Option::getExpirationDate));
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public LocalDate getNow() {
        return now;
    }

    public List<Option> getOptions(LocalDate date) {
        return options.getOrDefault(date, Collections.emptyList());
    }

    public List<Option> getOptions() {
        return options.entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toList());
    }

    public List<LocalDate> getAvailableDates() {
        return options.keySet().stream().sorted().collect(Collectors.toList());
    }

    @JsonIgnore
    public double getVolatility(LocalDate date) {
        return options.get(date).stream()
                .filter(o -> o.getStrikePrice().subtract(getCurrentPrice()).abs().compareTo(BigDecimal.valueOf(5)) < 0)
                .mapToDouble(o -> o.getImpliedVolatility())
                .average()
                .getAsDouble();
    }

    @JsonPOJOBuilder
    public static final class Builder {
        private String ticker;
        private BigDecimal currentPrice;
        private LocalDate now;
        private List<Option> options;

        public static final Builder create() {
            return new Builder();
        }
        public Builder() {
        }

        public Builder withTicker(String val) {
            ticker = val;
            return this;
        }

        public Builder withCurrentPrice(BigDecimal val) {
            currentPrice = val;
            return this;
        }

        public Builder withNow(LocalDate val) {
            now = val;
            return this;
        }

        public Builder withOptions(List<Option> val) {
            options = val;
            return this;
        }

        public OptionBook build() {
            return new OptionBook(this);
        }
    }
}
