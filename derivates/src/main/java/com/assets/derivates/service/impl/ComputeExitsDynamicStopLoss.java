package com.assets.derivates.service.impl;

import com.assets.derivates.service.ComputeExitsService;
import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;

import java.math.BigDecimal;

public class ComputeExitsDynamicStopLoss implements ComputeExitsService {

    private static final BigDecimal PERCENT_TO_MAXIMUM = BigDecimal.valueOf(0.05d);
    
    @Override
    public StockList computeExits(StockList entryPoints, StockList inputData) {
        StockList exits = new StockList(inputData.getTicker());
        
        BigDecimal currentMaximum = BigDecimal.ZERO;
        for(int i = 0; i < inputData.size(); i++){
            StockPrice currentValue = inputData.get(i);
            BigDecimal currentValuePrice = currentValue.getValue();
            
            currentMaximum = currentMaximum.compareTo(currentValuePrice) < 0 ? currentValuePrice : currentMaximum;
            
            if(currentValuePrice.compareTo(currentMaximum.subtract(currentValuePrice.multiply(PERCENT_TO_MAXIMUM))) < 0){
                exits.add(currentValue);
                currentMaximum = BigDecimal.ZERO;
            }
        }
        
        return exits;
    }

}
