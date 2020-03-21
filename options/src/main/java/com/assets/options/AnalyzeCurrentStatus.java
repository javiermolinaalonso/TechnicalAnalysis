package com.assets.options;

import com.assets.options.analyzers.SpreadAnalyzer;
import com.assets.options.entities.OptionBuilder;
import com.assets.options.entities.spread.OptionSpread;
import com.assets.options.entities.spread.SpreadFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnalyzeCurrentStatus {

    /**
     * Example of use
     * DebitCall 1 SPY 229.40 26 225 17.57 17.96 230 14.57 14.96
     * CreditCall 1 SPY 229.40 26 225 17.57 17.96 230 14.57 14.96
     * @param args
     */
    public static void main(String[] args) {
        OptionSpread spread = loadSpread(args);
        PrintUtils.print(spread, 0.15);
        printAnalysis(spread, args);
    }

    private static OptionSpread loadSpread(String[] args) {
        SpreadFactory spreadFactory = new SpreadFactory();
        String spreadType = args[0];
        int contracts = Integer.valueOf(args[1]);
        String ticker = args[2];
        double currentPrice = Double.parseDouble(args[3]);

        switch (spreadType) {
            case "DebitCall" :
                return spreadFactory.bullCallSpread(
                        OptionBuilder.create(ticker, currentPrice)
                                .withDaysToExpiry(Integer.valueOf(args[4]))
                                .withStrikePrice(Double.parseDouble(args[5]))
                                .withBidAsk(Double.parseDouble(args[6]), Double.parseDouble(args[7]))
                                .buildCall(),
                        OptionBuilder.create(ticker, currentPrice)
                                .withDaysToExpiry(Integer.valueOf(args[4]))
                                .withStrikePrice(Double.parseDouble(args[8]))
                                .withBidAsk(Double.parseDouble(args[9]), Double.parseDouble(args[10]))
                                .buildCall(),
                        contracts);
            case "CreditCall" :
                return spreadFactory.bearCallSpread(
                        OptionBuilder.create(ticker, currentPrice)
                                .withDaysToExpiry(Integer.valueOf(args[4]))
                                .withStrikePrice(Double.parseDouble(args[5]))
                                .withBidAsk(Double.parseDouble(args[6]), Double.parseDouble(args[7]))
                                .buildCall(),
                        OptionBuilder.create(ticker, currentPrice)
                                .withDaysToExpiry(Integer.valueOf(args[4]))
                                .withStrikePrice(Double.parseDouble(args[8]))
                                .withBidAsk(Double.parseDouble(args[9]), Double.parseDouble(args[10]))
                                .buildCall(),
                        contracts);
            default:
                throw new RuntimeException("Spread type not supported");
        }
    }

    private static void printAnalysis(OptionSpread spread, String[] args) {
        System.out.println(new SpreadAnalyzer().analyze(spread, new BigDecimal(args[3]), LocalDate.now()).toString());
    }
}
