package com.assets.derivates.service.impl;

import com.assets.derivates.entities.StockDerivateInstantValue;
import com.assets.derivates.service.ComputeEntranceService;
import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComputeBasicEntranceServiceWithStdDevImpl implements ComputeEntranceService {

    private static final Integer SHORT_MEAN = 7;
    private static final Integer LONG_MEAN = 30;
    private static final int VALUES_TO_CHECK = 10;
    @Override
    public StockList computeEntrances(final StockList input) {
        List<StockDerivateInstantValue> values = extractValues(input);
        
        List<StockPrice> entries = computeEntrances(values, input);
        
        return new StockList(entries, input.getTicker());
    }

    private List<StockDerivateInstantValue> extractValues(StockList input){
        StockList[] values = new StockList[6];
        values[0] = input;
        values[1] = input.getMean(SHORT_MEAN);
        values[2] = input.getMean(LONG_MEAN);
        values[3] = input.getFirstDerivate();
        values[4] = values[1].getFirstDerivate();
        values[5] = values[2].getFirstDerivate();
        
        return extractValuesAndFilterNulls(values);
    }

    private List<StockDerivateInstantValue> extractValuesAndFilterNulls(StockList[] values) {
        List<StockDerivateInstantValue> rList = new ArrayList<StockDerivateInstantValue>();
        for(StockPrice currentPrice : values[0]){
            Instant instant = currentPrice.getInstant();
            BigDecimal currentValue = currentPrice.getValue();
            BigDecimal shortMeanValue = values[1].getByInstant(instant) != null ? values[1].getByInstant(instant).getValue() : null;
            BigDecimal longMeanValue = values[2].getByInstant(instant) != null ? values[2].getByInstant(instant).getValue() : null;
            BigDecimal currentDerivate = values[3].getByInstant(instant) != null ? values[3].getByInstant(instant).getValue() : null;
            BigDecimal shortMeanDerivate = values[4].getByInstant(instant) != null ? values[4].getByInstant(instant).getValue() : null;
            BigDecimal longMeanDerivate = values[5].getByInstant(instant) != null ? values[5].getByInstant(instant).getValue() : null;
            
            rList.add(new StockDerivateInstantValue(instant, currentValue, shortMeanValue, longMeanValue, currentDerivate, shortMeanDerivate, longMeanDerivate));
        }
        
        return rList.parallelStream().filter(x -> x.getLongMeanDerivate() != null ).sequential().collect(Collectors.toList());
    }


    private List<StockPrice> computeEntrances(List<StockDerivateInstantValue> values, StockList input) {
        List<StockPrice> entryPoints = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            BigDecimal stdev = input.getStdDevLastSessions(LONG_MEAN, i);
            if(checkInputCondition(values, i, stdev)){
                StockDerivateInstantValue value = values.get(i);
                entryPoints.add(new StockPrice(input.getTicker(), value.getInstant(), value.getCurrentValue()));
            }
        }
        return entryPoints;
    }

    private boolean checkInputCondition(List<StockDerivateInstantValue> values, int index, BigDecimal stdDevMonth) {
        StockDerivateInstantValue value = values.get(index);
        boolean previousValuesAreFalse = isPreviousValuesFalse(values, index, stdDevMonth);
        return previousValuesAreFalse && checkInputCondition(value, stdDevMonth);
    }

    private boolean isPreviousValuesFalse(List<StockDerivateInstantValue> values, int index, BigDecimal stdDevMonth) {
        boolean value = false;
        for(int i = index - 1; i >= index - VALUES_TO_CHECK && i >= 0; i--){
            value = value || checkInputCondition(values.get(i), stdDevMonth);
        }
        return !value;
    }

    private boolean checkInputCondition(StockDerivateInstantValue value, BigDecimal stdDevMonth) {
        return  value.getCurrentValue().compareTo(value.getLongMeanValue().subtract(stdDevMonth)) < 0 
                && value.getCurrentDerivate().compareTo(BigDecimal.ZERO) > 0 
                && value.getShortMeanDerivate().compareTo(BigDecimal.ZERO) > 0;
    }
    
    
}
