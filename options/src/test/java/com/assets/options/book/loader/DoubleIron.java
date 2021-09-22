package com.assets.options.book.loader;

import com.assets.data.loader.impl.NasdaqDailyLoaderCsv;
import com.assets.options.OptionUtils;
import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.yahoo.OptionBookLoaderYahooOffline;
import com.assets.options.entities.CallOption;
import com.assets.options.entities.PutOption;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

public class DoubleIron {

    private static final Logger logger = LogManager.getLogger(OptionAnalyzeEvolution.class);

    private final SpreadFactory spreadFactory = new SpreadFactory();

    private final LocalDate from = LocalDate.of(2021, 1, 1);
    private final LocalDate targetExpirationDate = OptionUtils.getThirdFridayOfMonth(2021, Month.FEBRUARY);


    @Test
    void foo() throws IOException {
        double s1 = 365;
        double s2 = 385;
        double s3 = 385;
        double s4 = 405;

        Optional<OptionBook> optBook = OptionBookLoaderYahooOffline.load(from);
        if (optBook.isPresent()) {
            OptionBook book = optBook.get();
            for (int i = 340; i < 413; i++ ) {
                try {
                    CallOption callOption = book.getCallOption(targetExpirationDate, BigDecimal.valueOf(i));
                    PutOption putOption = book.getPutOption(targetExpirationDate, BigDecimal.valueOf(i));
                    logger.info("{}, {}, {}", book.getCurrentPrice(), i, callOption.getBid().add(putOption.getBid()));
                } catch (Exception e) {

                }
            }
        }
    }

}
