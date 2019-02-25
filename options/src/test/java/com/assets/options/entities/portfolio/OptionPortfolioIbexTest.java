package com.assets.options.entities.portfolio;

import com.assets.options.entities.Option;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.OptionType;
import com.assets.options.entities.spread.VerticalCallSpread;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class OptionPortfolioIbexTest {

    LocalDate now = LocalDate.of(2016, 4, 25);
    LocalDate nearExpiry = LocalDate.of(2016, 5, 20);
    LocalDate farExpiry = LocalDate.of(2017, 4, 21);
    BigDecimal currentPrice = BigDecimal.valueOf(9200);
    BigDecimal longStrike = BigDecimal.valueOf(9400);
    BigDecimal longBid = BigDecimal.valueOf(73);
    BigDecimal longAsk = BigDecimal.valueOf(83);

    BigDecimal longFarBid = BigDecimal.valueOf(635);
    BigDecimal longFarAsk = BigDecimal.valueOf(685);

    BigDecimal shortStrike = BigDecimal.valueOf(9100);
    BigDecimal shortBid = BigDecimal.valueOf(198);
    BigDecimal shortAsk = BigDecimal.valueOf(210);

    @Test
    public void testGivenCallRatioSpreadExpectHighThetaLowDelta() throws Exception {
        Option longOption = new Option("IBEX", currentPrice, longStrike, longBid, longAsk, OptionType.CALL, now, nearExpiry, 0.02d);
        Option shortOption = new Option("IBEX", currentPrice, shortStrike, shortBid, shortAsk, OptionType.CALL, now, nearExpiry, 0.02d);
        OptionTrade longTrade = new OptionTrade(longOption, 1, "IBEX", BigDecimal.ONE, true);
        OptionTrade shortTrade = new OptionTrade(shortOption, -2, "IBEX", BigDecimal.ONE, true);
        System.out.println("Long " + longOption.getGreeks());
        System.out.println("Short " + shortOption.getGreeks());
        OptionPortfolio optionPortfolio = new OptionPortfolio(Arrays.asList(longTrade, shortTrade));

        System.out.println(optionPortfolio.getGreeks());
    }

    @Test
    public void testGivenShortCallBearSpread() throws Exception {
        Option longOption = new Option("IBEX", currentPrice, longStrike, longBid, longAsk, OptionType.CALL, now, nearExpiry, 0.02d);
        Option shortOption = new Option("IBEX", currentPrice, shortStrike, shortBid, shortAsk, OptionType.CALL, now, nearExpiry, 0.02d);
        OptionTrade longTrade = new OptionTrade(longOption, 1, "IBEX", BigDecimal.ONE, true);
        OptionTrade shortTrade = new OptionTrade(shortOption, -1, "IBEX", BigDecimal.ONE, true);
        VerticalCallSpread bearCallSpread = new VerticalCallSpread(currentPrice, shortStrike, longStrike, now, nearExpiry, longOption.getVolatility(), 0.02, BigDecimal.valueOf(2), "IBEX", 1, true);
        bearCallSpread.printSpread(8500d, 9500d, 50d);
        System.out.println("Long " + longOption.getGreeks());
        System.out.println("Short " + shortOption.getGreeks());
        OptionPortfolio optionPortfolio = new OptionPortfolio(Arrays.asList(longTrade, shortTrade));

        System.out.println(optionPortfolio.getGreeks());
    }

    @Test
    public void testGivenCallRatioCalendarSpreadExpectHighThetaLowDelta() throws Exception {
        Option longOption = new Option("IBEX", currentPrice, longStrike, longFarBid, longFarAsk, OptionType.CALL, now, farExpiry, 0.02d);
        Option shortOption = new Option("IBEX", currentPrice, shortStrike, shortBid, shortAsk, OptionType.CALL, now, nearExpiry, 0.02d);
        OptionTrade longTrade = new OptionTrade(longOption, 1, "IBEX", BigDecimal.ONE, true);
        OptionTrade shortTrade = new OptionTrade(shortOption, -2, "IBEX", BigDecimal.ONE, true);
        System.out.println("Long " + longOption.getGreeks());
        System.out.println("Short " + shortOption.getGreeks());
        OptionPortfolio optionPortfolio = new OptionPortfolio(Arrays.asList(longTrade, shortTrade));

        System.out.println(optionPortfolio.getGreeks());
    }
}
