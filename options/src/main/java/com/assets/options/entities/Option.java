package com.assets.options.entities;

import com.assets.options.blackscholes.BlackScholesGreeks;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Option {

    private static final Double IMPLIED_VOLATILITY_ERROR = 0.01d;

    protected final BigDecimal currentPrice;
    protected final BigDecimal strikePrice;
    protected final LocalDate currentDate;
    protected final LocalDate expirationDate;
    protected final Double volatility;
    protected final Double riskFree;

    protected final BigDecimal premium;
    protected final Double delta;
    protected final Double gamma;
    protected final Double vega;
    protected final Double theta;
    protected final Double rho;

    public Option(BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        this.currentPrice = currentPrice;
        this.strikePrice = strikePrice;
        this.volatility = volatility;
        this.riskFree = riskFree;
        this.currentDate = now;
        this.expirationDate = expirationDate;
        double yearsToExpiry = getDaysToExpiry() / 365d;
        double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, volatility);
        premium = BigDecimal.valueOf(results[0]);
        delta = results[1];
        gamma = results[2];
        vega = results[3];
        theta = results[4];
        rho = results[5];
    }

    public Option(BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal premium, LocalDate now, LocalDate expirationDate, Double riskFree) {
        this.currentPrice = currentPrice;
        this.strikePrice = strikePrice;
        this.premium = premium;
        this.riskFree = riskFree;
        this.currentDate = now;
        this.expirationDate = expirationDate;
        double yearsToExpiry = getDaysToExpiry() / 365d;
        double[] results = calculateVolatility(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, premium.doubleValue(), 0.3d);
        this.volatility = results[0];
        delta = results[1];
        gamma = results[2];
        vega = results[3];
        theta = results[4];
        rho = results[5];
    }

    private double[] calculateVolatility(boolean call, double currentPrice, double strikePrice, Double riskFree, double yearsToExpiry, double premium, double impliedVolatility) {
        double[] calculate = BlackScholesGreeks.calculate(call, currentPrice, strikePrice, riskFree, yearsToExpiry, impliedVolatility);
        double calculatedPremium = calculate[0];
        if(Math.abs(calculatedPremium - premium) < IMPLIED_VOLATILITY_ERROR) {
            return new double[]{impliedVolatility, calculate[1], calculate[2], calculate[3], calculate[4], calculate[5]};
        } else if (calculatedPremium > premium) {
            return calculateVolatility(call, currentPrice, strikePrice, riskFree, yearsToExpiry, premium, impliedVolatility - 0.01d);
        } else {
            return calculateVolatility(call, currentPrice, strikePrice, riskFree, yearsToExpiry, premium, impliedVolatility + 0.01d);
        }
    }

    private int convertDays(LocalDate expiration, LocalDate now) {
        return (int) ChronoUnit.DAYS.between(now, expiration);
    }

    protected abstract boolean isCall();

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

    public Double getVolatility() {
        return volatility;
    }

    public Double getRiskFree() {
        return riskFree;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public Double getDelta() {
        return delta;
    }

    public Double getGamma() {
        return gamma;
    }

    public Double getVega() {
        return vega;
    }

    public Double getTheta() {
        return theta;
    }

    public Double getRho() {
        return rho;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("currentPrice", currentPrice)
                .append("strikePrice", strikePrice)
                .append("daysToExpiry", getDaysToExpiry())
                .append("volatility", volatility)
                .append("riskFree", riskFree)
                .append("premium", premium)
                .append("delta", delta)
                .append("gamma", gamma)
                .append("vega", vega)
                .append("theta", theta)
                .append("rho", rho)
                .toString();
    }
}
