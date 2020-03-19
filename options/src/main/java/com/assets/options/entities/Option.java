package com.assets.options.entities;

import com.assets.options.blackscholes.BlackScholesGreeks;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Option implements Comparable<Option>{

    private static final Double IMPLIED_VOLATILITY_ERROR = 0.0001d;
    private static final double MAX_IV = 1d;
    private static final double MIN_IV = 0.01d;
    private static final int MAX_IV_STEPS = 25;

    protected final String ticker;
    protected final OptionType optionType;
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

    public Option(String ticker, OptionType optionType, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double impliedVolatility, Double riskFree) {
        this(ticker, currentPrice, strikePrice, optionType, now, expirationDate, riskFree);
        this.impliedVolatility = impliedVolatility;
        double yearsToExpiry = getDaysToExpiry() / 365d;
        double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, impliedVolatility);
        premium = BigDecimal.valueOf(results[0]);
        bid = premium;
        ask = premium;
        greeks = new Greeks(results[1],results[2],results[3], results[4]/365d, results[5]);
    }

    public Option(String ticker, OptionType optionType, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, LocalDate now, LocalDate expirationDate, Double riskFree) {
        this(ticker, currentPrice, strikePrice, optionType, now, expirationDate, riskFree);
        double yearsToExpiry = getDaysToExpiry() / 365d;

        this.premium = bid.add(ask).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
        this.bid = bid;
        this.ask = ask;

        double[] results = calculateVolatility(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, premium.doubleValue(), MAX_IV, MAX_IV, MIN_IV, MAX_IV_STEPS);
        this.impliedVolatility = results[0];
        greeks = new Greeks(results[1],results[2],results[3], results[4] / 365d, results[5]);
    }

    private Option(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, OptionType optionType, LocalDate now, LocalDate expirationDate, Double riskFree) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.strikePrice = strikePrice;
        this.optionType = optionType;
        this.riskFree = riskFree;
        this.currentDate = now;
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

    public boolean isCall() {
        return OptionType.CALL.equals(optionType);
    }

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

    public BigDecimal getPremium() {
        return premium;
    }

    public Greeks getGreeks() {
        return greeks;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %.2f, %s] at %.2f, %s, [%.2f, %.2f], %s",
                ticker, isCall() ? "C" : "P", currentPrice, currentDate.toString(), strikePrice,
                expirationDate.toString(), bid, ask, greeks);
    }

    @Override
    public int compareTo(@NotNull Option o) {
        return getStrikePrice().compareTo(o.getStrikePrice());
    }
}
