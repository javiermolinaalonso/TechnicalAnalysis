package com.assets.options.entities.portfolio;

import com.assets.options.entities.Greeks;
import com.assets.options.entities.OptionTrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OptionPortfolio {

    //All trades should have the same ticker
    private List<OptionTrade> trades;

    public OptionPortfolio(List<OptionTrade> trades) {
        this.trades = trades;
    }

    public List<OptionTrade> getTrades() {
        return trades;
    }

    public BigDecimal getCost(LocalDate when, BigDecimal currentPrice, double volatility) {
        return getOptionTrades(when, currentPrice, volatility)
                .stream()
                .map(OptionTrade::getCost)
                .reduce((c1, c2) -> c1 = c1.add(c2))
                .get();
    }

    public Greeks getGreeks(LocalDate when, BigDecimal currentPrice, double volatility) {
        List<OptionTrade> updatedTrades = getOptionTrades(when, currentPrice, volatility);
        return getGreeks(updatedTrades);
    }

    private List<OptionTrade> getOptionTrades(LocalDate when, BigDecimal currentPrice, double volatility) {
        return this.trades
                    .parallelStream()
                    .filter(trade -> !trade.getOption().getExpirationDate().isBefore(when))
                    .map(trade -> new OptionTrade(
                            trade.getExpectedOption(currentPrice, when, volatility),
                            trade.getContracts(),
                            trade.getTicker(),
                            trade.getContractComission(),
                            trade.isMini()))
                    .collect(Collectors.toList());
    }

    public Greeks getGreeks() {
        return getGreeks(trades);
    }

    private Greeks getGreeks(List<OptionTrade> trades) {
        double delta = 0d;
        double gamma = 0d;
        double theta = 0d;
        double vega = 0d;
        double rho = 0d;
        for (OptionTrade trade : trades) {
            Greeks greeks = trade.getOption().getGreeks();
            delta += greeks.getDelta() * trade.getContracts();
            gamma += greeks.getGamma() * trade.getContracts();
            theta += greeks.getTheta() * trade.getContracts();
            vega += greeks.getVega() * trade.getContracts();
            rho += greeks.getRho() * Math.abs(trade.getContracts());
        }
        return new Greeks(delta, gamma, vega, theta, rho);
    }

    public void add(List<OptionTrade> optionTrades) {
        trades.addAll(optionTrades);
    }

    public void setTrades(List<OptionTrade> trades) {
        this.trades = trades;
    }
}
