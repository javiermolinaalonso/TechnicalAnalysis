package com.assets.portfolio.entities.investment;

import com.assets.investment.entities.InvestmentAction;
import com.assets.investment.entities.InvestmentActionEnum;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class TestInvestmentAction {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAmountInvestedBuy() {
        InvestmentAction action = new InvestmentAction("TEST", InvestmentActionEnum.BUY, Instant.now(), new BigDecimal(20), 10);
        assertEquals(200, action.getAmountInvested().intValue());
    }

    @Test
    public void testAmountInvestedSell() {
        InvestmentAction action = new InvestmentAction("TEST", InvestmentActionEnum.SELL, Instant.now(), new BigDecimal(20), 10);
        assertEquals(-200, action.getAmountInvested().intValue());
    }
}
