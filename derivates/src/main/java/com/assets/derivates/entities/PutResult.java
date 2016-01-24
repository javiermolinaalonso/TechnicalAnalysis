package com.assets.derivates.entities;

import java.time.LocalDate;

public class PutResult {

    private final Double initialPremium;
    private final Double finalPremium;
    private final Double maxPremium;
    private final Double strikePrice;
    private final Double initialPrice;
    private final Double initialVolatility;
    private final Double maxVolatility;
    private final LocalDate start;
    private final LocalDate end;
    private final LocalDate strikeDate;

    private PutResult(Builder builder) {
        initialPremium = builder.initialPremium;
        finalPremium = builder.finalPremium;
        maxPremium = builder.maxPremium;
        strikePrice = builder.strikePrice;
        initialPrice = builder.initialPrice;
        initialVolatility = builder.initialVolatility;
        maxVolatility = builder.maxVolatility;
        start = builder.start;
        end = builder.end;
        strikeDate = builder.strikeDate;
    }

    public Double getInitialPremium() {
        return initialPremium;
    }

    public Double getMaxPremium() {
        return maxPremium;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate getStrikeDate() {
        return strikeDate;
    }

    public Double getStrikePrice() {
        return strikePrice;
    }

    public double getBenefit() {
        return initialPremium - finalPremium;
    }

    @Override
    public String toString() {
        return String.format("{%.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %s, %s, %s}",
                initialPremium,
                finalPremium,
                maxPremium,
                initialPremium - maxPremium,
                strikePrice,
                initialPrice,
                initialVolatility,
                maxVolatility,
                start,
                end,
                strikeDate);
    }


    public static final class Builder {
        private Double initialPremium;
        private Double finalPremium;
        private Double maxPremium;
        private Double strikePrice;
        private Double initialPrice;
        private Double initialVolatility;
        private Double maxVolatility;
        private LocalDate start;
        private LocalDate end;
        private LocalDate strikeDate;

        public Builder() {
        }

        public Builder(PutResult copy) {
            this.initialPremium = copy.initialPremium;
            this.finalPremium = copy.finalPremium;
            this.maxPremium = copy.maxPremium;
            this.strikePrice = copy.strikePrice;
            this.initialPrice = copy.initialPrice;
            this.initialVolatility = copy.initialVolatility;
            this.maxVolatility = copy.maxVolatility;
            this.start = copy.start;
            this.end = copy.end;
            this.strikeDate = copy.strikeDate;
        }

        public Builder withInitialPremium(Double val) {
            initialPremium = val;
            return this;
        }

        public Builder withFinalPremium(Double val) {
            finalPremium = val;
            return this;
        }

        public Builder withMaxPremium(Double val) {
            maxPremium = val;
            return this;
        }

        public Builder withStrikePrice(Double val) {
            strikePrice = val;
            return this;
        }

        public Builder withInitialPrice(Double val) {
            initialPrice = val;
            return this;
        }

        public Builder withInitialVolatility(Double val) {
            initialVolatility = val;
            return this;
        }

        public Builder withMaxVolatility(Double val) {
            maxVolatility = val;
            return this;
        }

        public Builder withStartDate(LocalDate val) {
            start = val;
            return this;
        }

        public Builder withEndDate(LocalDate val) {
            end = val;
            return this;
        }

        public Builder withStrikeDate(LocalDate val) {
            strikeDate = val;
            return this;
        }

        public PutResult build() {
            return new PutResult(this);
        }
    }
}
