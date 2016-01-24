package com.assets.derivates;

import com.assets.data.loader.impl.DataDailyLoaderCsv;
import com.assets.derivates.entities.NakedPutVolatilityStrategyResult;
import com.assets.derivates.service.impl.NakedPutSellStrategyImpl;
import com.assets.entities.Candlestick;
import com.assets.options.impl.OptionsCalculatorCandlestick;
import com.assets.options.impl.VolatilityCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class OptionsVolatility {

    static int step = 22;

    NakedPutSellStrategyImpl nakedPutSellStrategy;
    ExecutorService service;

    public OptionsVolatility() {
        service = Executors.newFixedThreadPool(1);
//        service = Executors.newSingleThreadExecutor();
        VolatilityCalculator volatilityCalculator = new VolatilityCalculator();
        OptionsCalculatorCandlestick optionsCalculator = new OptionsCalculatorCandlestick(volatilityCalculator);
        nakedPutSellStrategy = new NakedPutSellStrategyImpl(volatilityCalculator, optionsCalculator);
    }

    private void calculateMonthlyVolatilities(List<Candlestick> values, File output) throws ExecutionException, InterruptedException, IOException {
        List<Future<NakedPutVolatilityStrategyResult>> results = new ArrayList<>();
        int calculations = 0;
        for(double vStart = 0.3; vStart < 0.6; vStart+=0.05) {
            for(double vEnd = 0.2; vEnd < 0.25; vEnd+=0.02) {
                for(double strikeDistance = 0.8; strikeDistance < 0.9; strikeDistance+=0.02) {
                    Future<NakedPutVolatilityStrategyResult> value = service.submit(new StrategyCallable(vStart, vEnd, strikeDistance, values));
                    results.add(value);
                    calculations++;
                }
            }
        }
//        writeToFile(output, results, calculations);
        for (Future<NakedPutVolatilityStrategyResult> result : results) {
            System.out.println(result.get().toCsvString());
        }

        service.shutdown();
    }

    private void writeToFile(File output, List<Future<NakedPutVolatilityStrategyResult>> results, float calculations) throws FileNotFoundException, InterruptedException, ExecutionException {
        PrintWriter printWriter = new PrintWriter(output);
        printWriter.println("Result, VolatStart, VolatEnd, Strike distance");
        int currentPrint = 0;
        for (Future<NakedPutVolatilityStrategyResult> result : results) {
            printWriter.println(result.get().toCsvString());
            System.out.println(String.format("%.2f%%", (float)currentPrint * 100 / calculations));
            currentPrint++;
        }
        printWriter.flush();
        printWriter.close();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, URISyntaxException {
        URL resource = OptionsVolatility.class.getClassLoader().getResource("FSLR2000-2016.csv");

        DataDailyLoaderCsv loader = new DataDailyLoaderCsv();
        List<Candlestick> values = loader.loadData(resource.getPath());
        Collections.sort(values, (x, y) -> x.getInitialInstant().compareTo(y.getInitialInstant()));

        File f = new File("/Users/javiermolina/projects/TechnicalAnalysis/foo.csv");
        new OptionsVolatility().calculateMonthlyVolatilities(values, f);
    }

    private class StrategyCallable implements Callable<NakedPutVolatilityStrategyResult> {
        private final Double vStart;
        private final Double strikeDistance;
        private final Double vEnd;
        private final List<Candlestick> values;

        private StrategyCallable(Double vStart, Double vEnd, Double strikeDistance, List<Candlestick> values) {
            this.vStart = vStart;
            this.strikeDistance = strikeDistance;
            this.vEnd = vEnd;
            this.values = values;
        }

        @Override
        public NakedPutVolatilityStrategyResult call() throws Exception {
            return nakedPutSellStrategy.calculateStrategy(values, step, vStart, strikeDistance, vEnd);
        }
    }
}
