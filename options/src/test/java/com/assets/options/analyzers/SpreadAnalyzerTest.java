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
    public void name() {
        final CalendarCallSpread ibm = CalendarCallSpread.complexSpread(144.62, 145, LocalDate.now(), 2, 0.80688, 31, 0.231, "IBM", 0.90);
        final SpreadAnalyzerResult analyze = victim.analyze(ibm, new BigDecimal("144.62"), LocalDate.now());

        System.out.println(ibm);
        System.out.println(analyze);
    }

    @Test
    public void testCalendar() {
        OptionSpread spreadDay1 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 4), 14, 0.1, 44,0.23, "IBM", 0.32);
        OptionSpread spreadDay2 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 5), 13, 0.32, 43,0.23, "IBM", 0.32);
        OptionSpread spreadDay3 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 6), 12, 0.32, 42,0.23, "IBM", 0.32);
        OptionSpread spreadDay4 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 7), 11, 0.32, 41,0.23, "IBM", 0.32);
        OptionSpread spreadDay5 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 8), 10, 0.32, 40,0.23, "IBM", 0.32);
        OptionSpread spreadDay6 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 9), 9, 0.32, 39,0.23, "IBM", 0.32);
        OptionSpread spreadDay7 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 10), 8, 0.32, 38,0.23, "IBM", 0.32);
        OptionSpread spreadDay8 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 11), 7, 0.32, 37,0.23, "IBM", 0.32);
        OptionSpread spreadDay9 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 12), 6, 0.32, 36,0.23, "IBM", 0.32);
        OptionSpread spreadDay10 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 13), 5, 0.32, 35,0.23, "IBM", 0.32);
        OptionSpread spreadDay11 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 14), 4, 0.32, 34,0.23, "IBM", 0.32);
        OptionSpread spreadDay12 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 15), 3, 0.32, 33,0.23, "IBM", 0.32);
        OptionSpread spreadDay13 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 16), 2, 0.32, 32,0.23, "IBM", 0.32);
        OptionSpread spreadDay14 = CalendarCallSpread.complexSpread(145d, 145, LocalDate.of(2019, 4, 17),   1, 0.32, 31,0.23, "IBM", 0.32);

        System.out.println(spreadDay1);
        System.out.println(spreadDay2);
        System.out.println(spreadDay3);
        System.out.println(spreadDay4);
        System.out.println(spreadDay5);
        System.out.println(spreadDay6);
        System.out.println(spreadDay7);
        System.out.println(spreadDay8);
        System.out.println(spreadDay9);
        System.out.println(spreadDay10);
        System.out.println(spreadDay11);
        System.out.println(spreadDay12);
        System.out.println(spreadDay13);
        System.out.println(spreadDay14);


        System.out.println("Spread purchased 14 days before");
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 4)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 5)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 6)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 7)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 8)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 9)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 10)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 11)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 12)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 13)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 14)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 15)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 16)));
        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 17)));


        System.out.println("Spread purchased 13 days before");
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 5)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 6)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 7)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 8)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 9)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 10)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 11)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 12)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 13)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 14)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 15)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 16)));
        System.out.println(victim.analyze(spreadDay2, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 17)));

        System.out.println("Spread purchased 12 days before");
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 6)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 7)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 8)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 9)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 10)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 11)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 12)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 13)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 14)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 15)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 16)));
        System.out.println(victim.analyze(spreadDay3, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 17)));
    }

    @Test
    public void testCalendarWow() {
        OptionSpread spreadDay1 = CalendarCallSpread.complexSpread(142d, 145, LocalDate.of(2019, 4, 4), 14, 0.32, 44,0.23, "IBM", 0.32);
        OptionSpread spreadDay2 = CalendarCallSpread.complexSpread(141d, 145, LocalDate.of(2019, 4, 5), 13, 0.32, 43,0.23, "IBM", 0.32);
        OptionSpread spreadDay9 = CalendarCallSpread.complexSpread(144d, 145, LocalDate.of(2019, 4, 12), 6, 0.32, 36,0.23, "IBM", 0.32);
        System.out.println(spreadDay1);
        System.out.println(spreadDay2);
        System.out.println(spreadDay9);
//        System.out.println(victim.analyze(spreadDay1, BigDecimal.valueOf(145d), LocalDate.of(2019, 4, 4)));
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