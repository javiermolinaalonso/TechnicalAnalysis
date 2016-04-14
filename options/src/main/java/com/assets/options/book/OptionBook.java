package com.assets.options.book;

import com.assets.options.entities.Option;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OptionBook {

    private final String ticker;
    private final BigDecimal currentPrice;
    private final LocalDate now;
    private final List<Option> availableOptions;

    private OptionBook(Builder builder) {
        ticker = builder.ticker;
        currentPrice = builder.currentPrice;
        now = builder.now;
        availableOptions = builder.availableOptions;
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

    public List<Option> getAvailableOptions() {
        return availableOptions;
    }

    public static final class Builder {
        private String ticker;
        private BigDecimal currentPrice;
        private LocalDate now;
        private List<Option> availableOptions;

        public Builder() {
        }

        public Builder(OptionBook copy) {
            this.ticker = copy.ticker;
            this.currentPrice = copy.currentPrice;
            this.now = copy.now;
            this.availableOptions = copy.availableOptions;
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

        public Builder withAvailableOptions(List<Option> val) {
            availableOptions = val;
            return this;
        }

        public OptionBook build() {
            return new OptionBook(this);
        }
    }
}
