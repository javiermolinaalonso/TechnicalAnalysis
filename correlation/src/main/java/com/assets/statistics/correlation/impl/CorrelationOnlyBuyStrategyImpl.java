package com.assets.statistics.correlation.impl;

import com.assets.entities.StockPrice;
import com.assets.entities.exceptions.exceptions.StockNotFoundException;
import com.assets.investment.entities.InvestmentAction;
import com.assets.investment.entities.InvestmentActionEnum;
import com.assets.statistic.list.StockList;
import com.assets.statistics.correlation.CorrelationStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CorrelationOnlyBuyStrategyImpl implements CorrelationStrategy {

    private static final Integer AMOUNT_OF_SHARES = 1;
    
    @Override
    public List<InvestmentAction> calculateBenefit(Instant currentInstant, Instant nextInstant, Iterable<StockList> stocks) {
      //Must determine which stock to buy and sell
        Iterator<StockList> it = stocks.iterator();
        StockList stockList1 = it.next();
        StockList stockList2 = it.next();
        List<InvestmentAction> actions = new ArrayList<>();
        try{
            StockPrice s1 = loadInstant(stockList1, currentInstant, nextInstant);
            StockPrice currentS1Stock = stockList1.getByInstant(currentInstant);
            BigDecimal s1IncreasePercent = currentS1Stock.getValue().subtract(s1.getValue()).divide(s1.getValue(), 5, RoundingMode.HALF_DOWN);
            
            StockPrice s2 = loadInstant(stockList2, currentInstant, nextInstant);
            StockPrice currentS2Stock = stockList2.getByInstant(currentInstant);
            BigDecimal s2IncreasePercent = currentS2Stock.getValue().subtract(s2.getValue()).divide(s2.getValue(), 5, RoundingMode.HALF_DOWN);
            
            if(s1IncreasePercent.compareTo(s2IncreasePercent) <= 0){
                //S2 have been incremented more than s1, so buy s1
                actions.add(new InvestmentAction(currentS1Stock, InvestmentActionEnum.BUY, AMOUNT_OF_SHARES));
                actions.add(new InvestmentAction(stockList1.getByInstant(nextInstant), InvestmentActionEnum.SELL, AMOUNT_OF_SHARES));
            }else{
                actions.add(new InvestmentAction(currentS2Stock, InvestmentActionEnum.BUY, AMOUNT_OF_SHARES));
                actions.add(new InvestmentAction(stockList2.getByInstant(nextInstant), InvestmentActionEnum.SELL, AMOUNT_OF_SHARES));
            }
        }catch(StockNotFoundException snfe){
            //TODO This problem happens because of timezones and way to search...
        }catch(Exception e){
        }
        return actions;
    }

}
