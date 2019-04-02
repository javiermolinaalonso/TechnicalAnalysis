package com.assets.options.entities;

import com.assets.options.blackscholes.BlackScholesGreeks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Option {

    private static final Double IMPLIED_VOLATILITY_ERROR = 0.01d;

    protected final String ticker;
    protected final OptionType optionType;
    protected final BigDecimal currentPrice;
    protected final BigDecimal strikePrice;
    protected final LocalDate currentDate;
    protected final LocalDate expirationDate;
    protected final Double riskFree;

    protected Double volatility;
    protected BigDecimal bid;
    protected BigDecimal ask;

    protected BigDecimal premium;
    protected Greeks greeks;

    public Option(String ticker, OptionType optionType, BigDecimal currentPrice, BigDecimal strikePrice, LocalDate now, LocalDate expirationDate, Double volatility, Double riskFree) {
        this(ticker, currentPrice, strikePrice, optionType, now, expirationDate, riskFree);
        this.volatility = volatility;
        double yearsToExpiry = getDaysToExpiry() / 365d;
        double[] results = BlackScholesGreeks.calculate(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, volatility);
        premium = BigDecimal.valueOf(results[0]);
        bid = premium;
        ask = premium;
        greeks = new Greeks(results[1],results[2],results[3], results[4]/365d, results[5]);
    }

    public Option(String ticker, BigDecimal currentPrice, BigDecimal strikePrice, BigDecimal bid, BigDecimal ask, OptionType optionType, LocalDate now, LocalDate expirationDate, Double riskFree) {
        this(ticker, currentPrice, strikePrice, optionType, now, expirationDate, riskFree);
        double yearsToExpiry = getDaysToExpiry() / 365d;

        this.premium = bid.add(ask).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
        this.bid = bid;
        this.ask = ask;

        double[] results = calculateVolatility(isCall(), currentPrice.doubleValue(), strikePrice.doubleValue(), riskFree, yearsToExpiry, premium.doubleValue(), 0.3d, null);
        this.volatility = results[0];
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

    private double[] calculateVolatility(boolean call, double currentPrice, double strikePrice, Double riskFree, double yearsToExpiry, double premium, double impliedVolatility, Boolean up) {
        double[] calculate = BlackScholesGreeks.calculate(call, currentPrice, strikePrice, riskFree, yearsToExpiry, impliedVolatility);
        double calculatedPremium = calculate[0];
        double[] values = {impliedVolatility, calculate[1], calculate[2], calculate[3], calculate[4], calculate[5]};
        if (calculatedPremium == 0d || Math.abs(calculatedPremium - premium) < IMPLIED_VOLATILITY_ERROR) {
            return values;
        } else if (impliedVolatility > 0.7 || impliedVolatility <= 0.01) {
            return values;
        } else if (calculatedPremium > premium) {
            if (up != null && up) {
                return values;
            }
            return calculateVolatility(call, currentPrice, strikePrice, riskFree, yearsToExpiry, premium, impliedVolatility - 0.01d, false);
        } else {
            if (up != null && !up) {
                return values;
            }
            return calculateVolatility(call, currentPrice, strikePrice, riskFree, yearsToExpiry, premium, impliedVolatility + 0.01d, true);
        }
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

    public Double getVolatility() {
        return volatility;
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
}
