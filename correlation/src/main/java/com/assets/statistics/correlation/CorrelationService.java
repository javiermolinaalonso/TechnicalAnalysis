package com.assets.statistics.correlation;

import com.assets.investment.entities.InvestmentResult;
import com.assets.statistics.entities.CorrelationIntervalInputData;
import com.assets.statistics.entities.CorrelationTwoStocks;
import com.assets.statistics.entities.StockCorrelation;

import java.util.List;
import java.util.Map;

public interface CorrelationService {

    Map<CorrelationIntervalInputData, List<StockCorrelation>> computeCorrelationBetweenTwoStocks(CorrelationTwoStocks inputData, Iterable<CorrelationIntervalInputData> intervals);
    
    List<StockCorrelation> calculateBestIntervalCorrelationInDateRangeSortedByDate(CorrelationTwoStocks stockData, CorrelationIntervalInputData intervalData);
    
    InvestmentResult calculateCorrelationAlerts(List<StockCorrelation> correlations, CorrelationTwoStocks stockData, CorrelationIntervalInputData inputDataInterval);
}
