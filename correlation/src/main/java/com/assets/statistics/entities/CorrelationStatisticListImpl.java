package com.assets.statistics.entities;

import com.assets.statistic.entities.StatisticList;
import com.assets.statistic.exceptions.EmptyStatisticListException;
import com.assets.statistic.list.LambdaStatisticList;
import com.assets.statistic.list.StockList;
import com.assets.statistics.correlation.impl.CorrelationThread;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by javier on 27/09/15.
 */
public class CorrelationStatisticListImpl extends LambdaStatisticList implements CorrelationStatisticList<BigDecimal> {

    private static final BigDecimal SPLITS = new BigDecimal(999999999);

    public CorrelationStatisticListImpl(List<BigDecimal> origin) {
        super(origin);
    }

    public CorrelationStatisticListImpl(StockList origin) {
        super(origin.stream().map(x -> x.getValue()).collect(Collectors.toList()));
    }

    public BigDecimal getCorrelation(StatisticList<BigDecimal> otherList){
        BigDecimal dividend = getStdDev().multiply(otherList.getStdDev());
        if(dividend.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        return getCovariance(otherList).divide(dividend, 5, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getCovariance(StatisticList<BigDecimal> otherList) {
        if(list.isEmpty() || otherList.getList().isEmpty()){
            throw new EmptyStatisticListException();
        }
        BigDecimal xMean = getMean();
        BigDecimal yMean = otherList.getMean();
        BigDecimal N = new BigDecimal(Math.min(list.size(), otherList.getList().size()));
        if(N.compareTo(SPLITS) > 0){
            return calculateCovarianceMultithread(xMean, yMean, N.intValue(), otherList);
        }else{
            return calculateCovariance(xMean, yMean, N.intValue(), otherList);
        }
    }


    private BigDecimal calculateCovariance(BigDecimal xMean, BigDecimal yMean, Integer N, StatisticList<BigDecimal> otherList){
        BigDecimal sum = new BigDecimal(0);
        for(int i = 0; i < N.intValue(); i++) {
            sum = sum.add((list.get(i).subtract(xMean)).multiply(otherList.getList().get(i).subtract(yMean)).divide(new BigDecimal(N), 5, RoundingMode.HALF_DOWN));
        }
        return sum;
    }

    private BigDecimal calculateCovarianceMultithread(BigDecimal xMean, BigDecimal yMean, Integer N, StatisticList<BigDecimal> otherList) {
        Vector<BigDecimal> results = mapToList(xMean, yMean, N, otherList);
        return reduceToResult(results);
    }

    private BigDecimal reduceToResult(Vector<BigDecimal> results) {
        BigDecimal sum = new BigDecimal(0);
        for(BigDecimal partialResult : results){
            sum = sum.add(partialResult);
        }
        return sum;
    }

    private Vector<BigDecimal> mapToList(BigDecimal xMean, BigDecimal yMean, Integer N, StatisticList<BigDecimal> otherList) {
        Integer divisions = 16;
        Integer i = 0;
        Integer from = 0;
        Integer step = N / divisions;
        Vector<Future<BigDecimal>> futureResults = new Vector<>(divisions+1);
        ExecutorService service = Executors.newCachedThreadPool();
        do{
            Integer to = from + step;
            CorrelationThread th = new CorrelationThread(this.list, otherList.getList(), xMean, yMean, N, from, to);
            futureResults.add(i, service.submit(th));
            from = to;

            i++;
        }while(i < divisions);

        //Complete the tail
        CorrelationThread th = new CorrelationThread(this.list, otherList.getList(), xMean, yMean, N, from, N);
        futureResults.add(divisions, service.submit(th));
        Vector<BigDecimal> results = new Vector<BigDecimal>();
        for(Future<BigDecimal> r : futureResults){
            try {
                results.add(r.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        return results;
    }
}
