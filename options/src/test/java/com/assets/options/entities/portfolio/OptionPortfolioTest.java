package com.assets.options.entities.portfolio;

import com.assets.options.entities.Greeks;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.OptionType;
import com.assets.options.entities.spread.VerticalCallSpread;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class OptionPortfolioTest {

    LocalDate now = LocalDate.of(2004, 1, 1);
    LocalDate expiry = LocalDate.of(2005, 1, 22);
    BigDecimal currentPrice = BigDecimal.valueOf(32.89);
    BigDecimal strike = BigDecimal.valueOf(32.5);
    BigDecimal bid = BigDecimal.valueOf(4.3);
    BigDecimal ask = BigDecimal.valueOf(4.4);

    @Test
    public void testGivenOneTradePortfolioExpectSameGreeksThanOption() throws Exception {
        Option option = new Option("INTC", currentPrice, strike, bid, ask, OptionType.CALL, now, expiry, 0.02d);
        OptionTrade optionTrade = new OptionTrade(option, 1, "INTC", BigDecimal.ONE, false);
        OptionPortfolio optionPortfolio = new OptionPortfolio(Arrays.asList(optionTrade));

        Greeks greeks = optionPortfolio.getGreeks();

        assertEquals(option.getGreeks().getDelta(), greeks.getDelta(), 0.001d);
        assertEquals(option.getGreeks().getGamma(), greeks.getGamma(), 0.001d);
        assertEquals(option.getGreeks().getVega(), greeks.getVega(), 0.001d);
        assertEquals(option.getGreeks().getTheta(), greeks.getTheta(), 0.001d);
        assertEquals(option.getGreeks().getRho(), greeks.getRho(), 0.001d);
    }

    @Test
    public void testGivenOneTradeSoldPortfolioExpectSameGreeksInCorrectDirectionThanOption() throws Exception {
        Option option = new Option("INTC", currentPrice, strike, bid, ask, OptionType.CALL, now, expiry, 0.02d);
        OptionTrade optionTrade = new OptionTrade(option, -1, "INTC", BigDecimal.ONE, false);
        OptionPortfolio optionPortfolio = new OptionPortfolio(Arrays.asList(optionTrade));

        Greeks greeks = optionPortfolio.getGreeks();

        assertEquals(option.getGreeks().getDelta() * -1, greeks.getDelta(), 0.001d);
        assertEquals(option.getGreeks().getGamma(), greeks.getGamma(), 0.001d);
        assertEquals(option.getGreeks().getVega(), greeks.getVega(), 0.001d);
        assertEquals(option.getGreeks().getTheta() * -1, greeks.getTheta(), 0.001d);
        assertEquals(option.getGreeks().getRho(), greeks.getRho(), 0.001d);
    }


    @Test
    public void testGivenSpreadExpectAverageGreeks() throws Exception {
        BigDecimal highStrike = BigDecimal.valueOf(35);
        BigDecimal highBid = BigDecimal.valueOf(4.4);
        BigDecimal lowStrike = BigDecimal.valueOf(32.5);
        BigDecimal lowAsk = BigDecimal.valueOf(3.2);
        VerticalCallSpread spread = new VerticalCallSpread(currentPrice, lowStrike, highStrike, now, expiry, lowAsk, highBid, 0.02d, BigDecimal.ONE, "INTC", 1, false);
        OptionPortfolio optionPortfolio = new OptionPortfolio(spread.getOptionTrades());
//        ["INTC", C, 32,89, 2004-01-01] at 32,50, 2005-01-22, [3,20, 3,20], Greeks: [0,6053, 0,0598, -1,5044, 13,0371, 17,8299]
//        ["INTC", C, 32,89, 2004-01-01] at 35,00, 2005-01-22, [4,40, 4,40], Greeks: [0,5331, 0,0317, -2,6117, 13,4645, 13,9088]

        Greeks greeks = optionPortfolio.getGreeks();

//        [0,0723, 0,0281, 1,1072, 26,5016, 31,7387]
        assertEquals(0.07, greeks.getDelta(), 0.01d);
        assertEquals(0.02, greeks.getGamma(), 0.01d);
        assertEquals(1.1, greeks.getTheta(), 0.01d);
    }

}