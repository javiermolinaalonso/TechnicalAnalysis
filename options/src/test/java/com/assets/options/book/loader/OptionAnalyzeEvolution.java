package com.assets.options.book.loader;

import com.assets.data.loader.impl.NasdaqDailyLoaderCsv;
import com.assets.options.OptionUtils;
import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOffline;
import com.assets.options.entities.spread.SpreadFactory;
import com.assets.options.entities.spread.neutral.IronCondorSpread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ChopIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

public class OptionAnalyzeEvolution {

    private static final Logger logger = LogManager.getLogger(OptionAnalyzeEvolution.class);

    private final SpreadFactory spreadFactory = new SpreadFactory();
    private final NasdaqDailyLoaderCsv stockLoader = new NasdaqDailyLoaderCsv();

    private final LocalDate from = LocalDate.of(2021, 1, 1);
    private final LocalDate targetExpirationDate = OptionUtils.getThirdFridayOfMonth(2021, Month.MARCH);

    @Test
    void foo() throws IOException {
        double s1 = 395;
        double s2 = 400;
        double s3 = 400;
        double s4 = 405;

        BarSeries barSeries = stockLoader.loadData("/Users/javiermolina/stockHistory/SPY24.csv");
        StandardDeviationIndicator stdDevIndicator = new StandardDeviationIndicator(new ClosePriceIndicator(barSeries), 22);
        ChopIndicator chop = new ChopIndicator(barSeries, 14, 1);
        logger.info("Iron condor spread {}/{}/{}/{}", s1, s2, s3, s4);
        logger.info("Date, Price, Premium, Chop, ImpliedVol, HistVol, Delta, Gamma, Theta, Vega, Rho");
        for (LocalDate date = from; date.isBefore(targetExpirationDate); date = date.plusDays(1)) {
            Optional<OptionBook> optBook = OptionBookLoaderYahooOffline.load(date);
            if (optBook.isPresent()) {
                OptionBook book = optBook.get();
                IronCondorSpread ic = spreadFactory.ironCondor(book, targetExpirationDate, s1, s2, s3, s4);
                int index = getIndexOf(barSeries, date);
                Num stdDev = stdDevIndicator.getValue(index);
                Num annualizedStdDev = stdDev.multipliedBy(DoubleNum.valueOf(Math.sqrt(12))).dividedBy(DoubleNum.valueOf(100));
                Num chopValue = chop.calculate(index);
                logger.info("{}, {}, {}, {}, {}, {}, {}", date, book.getCurrentPrice(), ic.getCost(), chopValue, ic.getVolatility(), annualizedStdDev, ic.getGreeks().toString());
            }
        }
    }

    private int getIndexOf(BarSeries series, LocalDate date) {
        for (int i = 0; i < series.getBarCount(); i++) {
            if (series.getBar(i).getEndTime().toLocalDate().isEqual(date)) {
                return i;
            }
        }
        return -1;
    }
}
