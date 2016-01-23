package com.assets.entities;

import com.assets.entities.exceptions.InvalidCandlestickValueException;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.time.*;

/**
 * Created by javier on 27/09/15.
 */
public class Candlestick {

    private final BigDecimal initialPrice;
    private final BigDecimal finalPrice;
    private final BigDecimal maxPrice;
    private final BigDecimal minPrice;
    private final Duration duration;
    private final Instant initialInstant;

    private Candlestick(Builder builder) {
        initialPrice = Validate.notNull(builder.initialPrice);
        finalPrice = Validate.notNull(builder.finalPrice);
        maxPrice = Validate.notNull(builder.maxPrice);
        minPrice = Validate.notNull(builder.minPrice);
        duration = Validate.notNull(builder.period);
        initialInstant = Validate.notNull(builder.initialInstant);
        if(maxPrice.compareTo(minPrice) < 0) {
            throw new InvalidCandlestickValueException();
        }
        if(maxPrice.compareTo(initialPrice) < 0) {
            throw new InvalidCandlestickValueException();
        }
        if(maxPrice.compareTo(finalPrice) < 0) {
            throw new InvalidCandlestickValueException();
        }
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getInitialInstant() {
        return initialInstant;
    }

    public LocalDate getDate() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(initialInstant, ZoneId.systemDefault());
        return localDateTime.toLocalDate();
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("initialPrice", initialPrice)
                .append("finalPrice", finalPrice)
                .append("maxPrice", maxPrice)
                .append("minPrice", minPrice)
                .append("duration", duration)
                .append("initialInstant", initialInstant)
                .toString();
    }


    public static final class Builder {

        private BigDecimal initialPrice;
        private BigDecimal finalPrice;
        private BigDecimal maxPrice;
        private BigDecimal minPrice;
        private Duration period;
        private Instant initialInstant;

        public Builder() {
        }

        public Builder(Candlestick copy) {
            initialPrice = copy.initialPrice;
            finalPrice = copy.finalPrice;
            maxPrice = copy.maxPrice;
            minPrice = copy.minPrice;
            period = copy.duration;
            initialInstant = copy.initialInstant;
        }

        public Builder withInitialPrice(BigDecimal initialPrice) {
            this.initialPrice = initialPrice;
            return this;
        }

        public Builder withFinalPrice(BigDecimal finalPrice) {
            this.finalPrice = finalPrice;
            return this;
        }

        public Builder withMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder withMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder withDuration(Duration period) {
            this.period = period;
            return this;
        }

        public Builder withInitialInstant(Instant initialInstant) {
            this.initialInstant = initialInstant;
            return this;
        }

        public Candlestick build() {
            return new Candlestick(this);
        }
    }
}
