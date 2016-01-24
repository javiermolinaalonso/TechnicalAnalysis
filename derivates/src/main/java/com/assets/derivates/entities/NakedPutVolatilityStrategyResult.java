package com.assets.derivates.entities;

import java.util.List;
import java.util.Locale;

public class NakedPutVolatilityStrategyResult {

    private final Double volatilityStart;
    private final Double volatilityEnd;
    private final Double strikeDistance;
    private final Double result;
    private final List<PutResult> results;

    private NakedPutVolatilityStrategyResult(Builder builder) {
        volatilityStart = builder.volatilityStart;
        volatilityEnd = builder.volatilityEnd;
        strikeDistance = builder.strikeDistance;
        result = builder.result;
        results = builder.results;
    }

    @Override
    public String toString() {
        return String.format("result:%.2f. vStart:%.2f, vEnd:%.2f, strike:%.2f", result / (double) results.size(), volatilityStart, volatilityEnd, strikeDistance);
    }

    public String toCsvString() {
        return String.format(Locale.US, "%.2f,%.2f,%.2f,%.2f, %s", result / (double) results.size(), volatilityStart, volatilityEnd, strikeDistance, results.toString());
    }


    public static final class Builder {
        private Double volatilityStart;
        private Double volatilityEnd;
        private Double strikeDistance;
        private Double result;
        private List<PutResult> results;

        public Builder() {
        }

        public Builder(NakedPutVolatilityStrategyResult copy) {
            this.volatilityStart = copy.volatilityStart;
            this.volatilityEnd = copy.volatilityEnd;
            this.strikeDistance = copy.strikeDistance;
            this.result = copy.result;
            this.results = copy.results;
        }

        public Builder withVolatilityStart(Double val) {
            volatilityStart = val;
            return this;
        }

        public Builder withVolatilityEnd(Double val) {
            volatilityEnd = val;
            return this;
        }

        public Builder withStrikeDistance(Double val) {
            strikeDistance = val;
            return this;
        }

        public Builder withResult(Double val) {
            result = val;
            return this;
        }

        public Builder withResults(List<PutResult> val) {
            results = val;
            return this;
        }

        public NakedPutVolatilityStrategyResult build() {
            return new NakedPutVolatilityStrategyResult(this);
        }
    }
}
