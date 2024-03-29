package com.assets.investment.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

public class InvestmentResult {

    private final Instant from;
    private final Instant to;
    private final InvestmentActions investmentActions;

    public InvestmentResult(Instant from, Instant to, InvestmentActions investmentActions) {
        super();
        this.from = from;
        this.to = to;
        this.investmentActions = investmentActions;
    }

    public Instant getFrom() {
        return this.from;
    }

    public Instant getTo() {
        return this.to;
    }

    public BigDecimal getBenefit(){
        return this.investmentActions.getBenefit();
    }
    
    public List<InvestmentAction> getActions(){
        return this.investmentActions.getActions();
    }

    public BigDecimal getAmountInvested() {
        return this.investmentActions.getAmountInvested();
    }
    
    public BigDecimal getProfitabilityPercent() {
        return this.investmentActions.getProfitability().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

}
