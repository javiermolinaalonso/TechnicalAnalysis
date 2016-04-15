package com.assets.options.main;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.OptionBookLoaderCsv;
import com.assets.options.entities.Option;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GreeksAnalyser {

    public static void main(String[] args) throws IOException {
        LocalDate now = LocalDate.of(2004, 1, 1);
        OptionBookLoaderCsv optionBookLoaderCsv = new OptionBookLoaderCsv(now);
        URL resource = GreeksAnalyser.class.getClassLoader().getResource("options_20040116INTC.csv");
        List<Option> options = optionBookLoaderCsv.loadData(resource.getPath());

        Map<LocalDate, List<Option>> intc = options.stream().filter(option -> option.getTicker().equals("INTC")).collect(Collectors.groupingBy(Option::getExpirationDate));
        for (Map.Entry<LocalDate, List<Option>> intcOptionBook : intc.entrySet()) {
            OptionBook optionBook = new OptionBook.Builder()
                    .withAvailableOptions(intcOptionBook.getValue())
                    .withCurrentPrice(BigDecimal.valueOf(32.89))
                    .withNow(now)
                    .withTicker("INTC")
                    .build();

            maximumTheta(optionBook);
        }
    }

    private static void maximumTheta(OptionBook optionBook) {
        optionBook.getAvailableOptions().sort((o1, o2) -> Double.compare(o1.getGreeks().getTheta(), o2.getGreeks().getTheta()));
        for (Option option : optionBook.getAvailableOptions()) {
            System.out.println(option);
            //Check maximum theta strategy
            //Simulate the expiration price by selling this option, choose only one by dateAdde
        }
    }
}
