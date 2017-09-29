package com.assets.statistic.list;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;


public class LambdaStatisticList extends AbstractStatisticList<BigDecimal> {

    public LambdaStatisticList(List<BigDecimal> origin){
        super(origin);
    }
    
    public BigDecimal computeMean() {
        return list.stream().reduce((x, y) -> x.add(y)).get().divide(new BigDecimal(list.size()), 5, RoundingMode.HALF_DOWN);
    }
    
    public BigDecimal getHighest() {
        return list.stream().max((a, b) -> a.compareTo(b)).get();
    }

    public BigDecimal getLowest() {
        return list.stream().min((a, b) -> a.compareTo(b)).get();
    }
    
    public BigDecimal getStdDev() {
        BigDecimal mean = getMean();
//        return Math.sqrt(stream().map(x -> x.subtract(mean).pow(2)).reduce((x, y) -> x.add(y)).get().divide(BigDecimal.valueOf(size() - 1), 5, RoundingMode.HALF_DOWN).doubleValue());
        return new BigDecimal(Math.sqrt(list.stream().map(x -> x.subtract(mean).pow(2)).reduce((x, y) -> x.add(y)).get().doubleValue() / list.size()));
    }
    
    public void reverse() {
        Collections.reverse(list);
    }

    @Override
    public List<BigDecimal> getList() {
        return list;
    }


}
