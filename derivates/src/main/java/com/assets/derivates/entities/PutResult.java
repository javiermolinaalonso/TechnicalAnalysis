package com.assets.derivates.entities;

import java.time.LocalDate;

public class PutResult {

    private final Double result;
    private final Double maxLoss;
    private final LocalDate start;
    private final LocalDate end;
    private final LocalDate strikeDate;
    private final Double strikePrice;

    private PutResult(Builder builder) {
        result = builder.result;
        maxLoss = builder.maxLoss;
        start = builder.start;
        end = builder.end;
        strikeDate = builder.strikeDate;
        strikePrice = builder.strikePrice;
    }

    public Double getResult() {
        return result;
    }

    public Double getMaxLoss() {
        return maxLoss;
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

    public static final class Builder {
        private Double result;
        private Double maxLoss;
        private LocalDate start;
        private LocalDate end;
        private LocalDate strikeDate;
        private Double strikePrice;

        public Builder() {
        }

        public Builder(PutResult copy) {
            this.result = copy.result;
            this.maxLoss = copy.maxLoss;
            this.start = copy.start;
            this.end = copy.end;
            this.strikeDate = copy.strikeDate;
            this.strikePrice = copy.strikePrice;
        }

        public Builder withResult(Double val) {
            result = val;
            return this;
        }

        public Builder withMaxLoss(Double val) {
            maxLoss = val;
            return this;
        }

        public Builder withStart(LocalDate val) {
            start = val;
            return this;
        }

        public Builder withEnd(LocalDate val) {
            end = val;
            return this;
        }

        public Builder withStrikeDate(LocalDate val) {
            strikeDate = val;
            return this;
        }

        public Builder withStrikePrice(Double val) {
            strikePrice = val;
            return this;
        }

        public PutResult build() {
            return new PutResult(this);
        }
    }
}
