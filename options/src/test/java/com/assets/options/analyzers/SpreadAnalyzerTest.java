package com.assets.options.analyzers;

import com.assets.options.entities.spread.CalendarCallSpread;
import com.assets.options.entities.spread.IronCondorSpread;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.options.entities.spread.VerticalPutSpread;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SpreadAnalyzerTest {

    public static final BigDecimal CURRENT_PRICE = BigDecimal.valueOf(142.11);
    private SpreadAnalyzer victim = new SpreadAnalyzer();

    @Test
    public void testCalendar() {
        OptionSpread spread = CalendarCallSpread.complexSpread(CURRENT_PRICE.doubleValue(), 145, LocalDate.of(2019, 4, 4), 14, 0.32, 44,0.23, "IBM", 0.32);
        OptionSpread evolvedSpread = CalendarCallSpread.complexSpread(CURRENT_PRICE.doubleValue(), 145, LocalDate.of(2019, 4, 10), 8, 0.4161, 38,0.2239, "IBM", 0.32);

        System.out.println(spread);
        System.out.println(evolvedSpread);
        System.out.println(victim.analyze(spread, CURRENT_PRICE, LocalDate.now()));
    }

    @Test
    public void testBullSpread() {
        final VerticalPutSpread spread = VerticalPutSpread.basicPutSpread(CURRENT_PRICE.doubleValue(), 140d, 145d, 45, 0.3, "IBM");
        System.out.println(victim.analyze(spread, CURRENT_PRICE, LocalDate.now()));
    }

//    Expectation: [winProbability=67.43%, riskRewardRatio=1.0506, averageWin=434.91, averageLoss=413.95, cutPoints=[771.5645], expectedTae=48.37%]
//    Purchased: {[702.00/710.00/762.00/786.00] @ 1996-12-07, [1.22/1.99/12.35/4.88], Max Loss:-1581.20, Max Win:818.80} purchased at 1996-11-27 with current price 755.00
//    Result=801.91, currentStockPrice=749.76
    @Test
    public void testIronCondor() {
        final IronCondorSpread spread = IronCondorSpread.basicIronCondor(755, 702, 710, 762, 786, 11, 0.05, "IBM");
        System.out.println(victim.analyze(spread, BigDecimal.valueOf(755), LocalDate.now()));
    }
}