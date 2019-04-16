package com.assets.options.impl;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.entities.Candlestick;
import com.assets.options.entities.CallOption;
import com.assets.options.entities.Greeks;
import com.assets.options.entities.OptionTrade;
import com.assets.options.entities.portfolio.OptionPortfolio;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CalendarSimulationTest {

    VolatilityCalculator volatilityCalculator;
    DataDailyLoaderCsv loader;
    List<Candlestick> candlesticks;

    OptionsCalculatorCandlestick calculator;

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getClassLoader().getResource("IBEXOneYear.csv");

        loader = new DataDailyLoaderCsv();
        candlesticks = loader.loadData(resource.getPath());
        Collections.reverse(candlesticks);

        candlesticks.stream().map(x -> x.getFinalPrice().doubleValue()).forEach(x -> System.out.println(String.format("%.0f", x)));
        volatilityCalculator = new VolatilityCalculator();
        calculator = new OptionsCalculatorCandlestick(volatilityCalculator);
    }

    @Test
    public void testName() throws Exception {
        double longCallVolatility = volatilityCalculator.getAnnualizedVolatility(candlesticks.subList(30, 60));
        double shortCallVolatility = volatilityCalculator.getAnnualizedVolatility(candlesticks.subList(30, 60));

        Candlestick candlestick = candlesticks.get(60);
        CallOption longCallOption = new CallOption(null, candlestick.getFinalPrice(), candlestick.getFinalPrice(), candlestick.getDate(),
                candlestick.getDate().plusYears(1), longCallVolatility, 0d);
        OptionTrade longCallTrade = new OptionTrade(longCallOption, 1, "IBX", BigDecimal.valueOf(2), true);
        CallOption shortCallOption = new CallOption(null, candlestick.getFinalPrice(), candlestick.getFinalPrice(), candlestick.getDate(),
                candlestick.getDate().plusMonths(1), shortCallVolatility, 0d);
        OptionTrade shortCallTrade = new OptionTrade(shortCallOption, -1, "IBX", BigDecimal.valueOf(2), true);

        OptionPortfolio portfolio = new OptionPortfolio(Arrays.asList(longCallTrade, shortCallTrade));

        System.out.println("Long Call: " + longCallOption);
        System.out.println("Short Call: " + shortCallOption);
        System.out.println("Portfolio: " + portfolio.getGreeks());
        System.out.println("***********");

        for(int i = 60; i < 200; i++) {
            double v = volatilityCalculator.getAnnualizedVolatility(candlesticks.subList(i - 30, i));
            Candlestick c = candlesticks.get(i);
            BigDecimal price = c.getFinalPrice();
            Greeks greeks = portfolio.getGreeks(c.getDate(), price, v);
            BigDecimal value = portfolio.getCost(c.getDate(), price, v);
            System.out.println(String.format("Stock price: %.2f, Volatility: %.2f, Value: %.2f. %s",
                    price, v, value.doubleValue(), greeks));
        }


//        double firstMonthVolatility = volatilityCalculator.getAnnualizedVolatility(candlesticks.subList(61, 82));
//        Candlestick firstMonthCandlestick = candlesticks.get(82);
//
//        System.out.println("Month portfolio: " + portfolio.getGreeks(firstMonthCandlestick.getDate(), firstMonthCandlestick.getFinalPrice(), firstMonthVolatility));
//        System.out.println("***********");
//        Option expectedValue = shortCallTrade.getExpectedOption(firstMonthCandlestick.getFinalPrice(), firstMonthCandlestick.getDate(), firstMonthVolatility);
//        Option longExpectedValue = longCallTrade.getExpectedOption(firstMonthCandlestick.getFinalPrice(), firstMonthCandlestick.getDate(), firstMonthVolatility);
//        System.out.println("Long Call first month: " + longExpectedValue);
//        System.out.println("Short call first month: " + expectedValue);
//        CallOption shortCallMonthOption = new CallOption(firstMonthCandlestick.getFinalPrice(),
//                candlestick.getFinalPrice(), firstMonthCandlestick.getDate(), firstMonthCandlestick.getDate().plusMonths(1),
//                firstMonthVolatility, 0d);
//        System.out.println("New short call first month: " + shortCallMonthOption);


    }
}
