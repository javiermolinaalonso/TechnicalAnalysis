package com.assets.options.entities;

import com.assets.options.blackscholes.BlackScholesGreeks;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "call")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CallOption.class, name = "true"),
        @JsonSubTypes.Type(value = PutOption.class, name = "false")
})
public abstract class Option implements Comparable<Option> {

    private static final Double IMPLIED_VOLATILITY_ERROR = 0.0001d;
    private static final double MAX_IV = 1d;
    private static final double MIN_IV = 0.01d;
    private static final int MAX_IV_STEPS = 25;

    protected final String ticker;
    protected final String optionSymbol;
    protected final BigDecimal currentPrice;
    protected final BigDecimal strikePrice;
    protected final LocalDate currentDate;
    protected final LocalDate expirationDate;
    protected final Double riskFree;

    protected Double impliedVolatility;
    protected BigDecimal bid;
    protected BigDecimal ask;

    protected BigDecimal premium;
    protected Greeks greeks;

    Option(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate currentDate, LocalDate expirationDate, Double impliedVolatility, Double riskFree) {
        this(ticker, "", currentPrice, strikePrice, currentDate, expirationDate, riskFree);
        this.impliedVolatility = impliedVolatility;
        double yearsToExpiry = getDaysToExpiry() / 365d;
        double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, impliedVolatility);
        premium = BigDecimal.valueOf(results[0]);
        bid = premium;
        ask = premium;
        greeks = new Greeks(results[1],results[2],results[3], results[4]/365d, results[5]);
    }

    Option(String ticker, String optionSymbol, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, LocalDate currentDate, LocalDate expirationDate, Double impliedVolatility, Double riskFree) {
        this(ticker, optionSymbol, currentPrice, strikePrice, currentDate, expirationDate, riskFree);
        double yearsToExpiry = getDaysToExpiry() / 365d;

        if (bid == null) {
            double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, impliedVolatility);
            this.premium = BigDecimal.valueOf(results[0]);
            this.bid = premium;
            this.ask = premium;
        } else {
            this.premium = bid.add(ask).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
            this.bid = bid;
            this.ask = ask;
        }

        if (impliedVolatility == null) {
            double[] results = calculateVolatility(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, premium.doubleValue(), MAX_IV, MAX_IV, MIN_IV, MAX_IV_STEPS);
            this.impliedVolatility = results[0];
            greeks = new Greeks(results[1],results[2],results[3], results[4] / 365d, results[5]);
        } else {
            this.impliedVolatility = impliedVolatility;
            double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, this.impliedVolatility);
            this.greeks = new Greeks(results[1],results[2],results[3], results[4]/365d, results[5]);
        }
    }

    private Option(String ticker, String optionSymbol, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate currentDate, LocalDate expirationDate, Double riskFree) {
        this.ticker = ticker;
        this.optionSymbol = optionSymbol;
        this.currentPrice = currentPrice;
        this.strikePrice = strikePrice;
        this.riskFree = riskFree;
        this.currentDate = currentDate;
        this.expirationDate = expirationDate;
    }

    private double[] calculateVolatility(boolean call, double currentPrice, double strikePrice, Double riskFree, double yearsToExpiry, double premium, double impliedVolatility, double maxIV, double minIV, int maxSteps) {
        double[] calculate = BlackScholesGreeks.calculate(call, currentPrice, strikePrice, riskFree, yearsToExpiry, impliedVolatility);
        double calculatedPremium = calculate[0];
        if (calculatedPremium > premium) {
            maxIV = impliedVolatility;
        } else {
            minIV = impliedVolatility;
        }
        impliedVolatility = (maxIV + minIV) / 2;
        if (maxSteps == 0 || isPremiumErrorAcceptable(premium, calculatedPremium)) {
            return new double[]{impliedVolatility, calculate[1], calculate[2], calculate[3], calculate[4], calculate[5]};
        } else {
            return calculateVolatility(call, currentPrice, strikePrice, riskFree, yearsToExpiry, premium, impliedVolatility, maxIV, minIV, maxSteps-1);
        }
    }

    private boolean isPremiumErrorAcceptable(double premium, double calculatedPremium) {
        return calculatedPremium == 0d || Math.abs(calculatedPremium - premium) < IMPLIED_VOLATILITY_ERROR;
    }

    private int convertDays(LocalDate expiration, LocalDate now) {
        return (int) ChronoUnit.DAYS.between(now, expiration);
    }

    public abstract boolean isCall();

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Double getDaysToExpiry() {
        return (double) convertDays(expirationDate, currentDate);
    }

    public Double getImpliedVolatility() {
        return impliedVolatility;
    }

    public Double getRiskFree() {
        return riskFree;
    }

//    public BigDecimal getPremium() {
//        return premium;
//    }



    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public Greeks getGreeks() {
        return greeks;
    }

    @JsonIgnore
    public boolean isTradeable() {
        return getBid().doubleValue() > 0.1 && getAsk().doubleValue() > 0.1 && getDaysToExpiry() > 0.1;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %.2f, %s] at %.2f, %s, [%.2f, %.2f, %.2f], %s, %s, %s",
                ticker, isCall() ? "C" : "P", currentPrice, currentDate.toString(), strikePrice,
                expirationDate.toString(), bid, ask, getAvgBidAsk(), greeks, optionSymbol, impliedVolatility);
    }

    public String getOptionSymbol() {
        return optionSymbol;
    }

    @Override
    public int compareTo(@NotNull Option o) {
        return getStrikePrice().compareTo(o.getStrikePrice());
    }

    public BigDecimal getAvgBidAsk() {
        return bid.add(ask).divide(BigDecimal.valueOf(2.0), 2, RoundingMode.HALF_UP);
    }
}
