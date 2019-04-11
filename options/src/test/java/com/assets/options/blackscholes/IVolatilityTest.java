package com.assets.options.blackscholes;

import com.assets.options.entities.CallOption;
import com.assets.options.entities.PutOption;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class IVolatilityTest {

    @Test
    public void volatilityIsProperlyCalculated_whenGivenOptionPrice() {
        final CallOption callV = new CallOption("IWM", 143.45, 144d, 3.7d, LocalDate.of(2019, 1, 16), LocalDate.of(2019, 3, 1), 0.015);
        final PutOption putV = new PutOption("IWM", 143.45, 144d, 4.16d, LocalDate.of(2019, 1, 16), LocalDate.of(2019, 3, 1), 0.015);
        assertThat(callV.getVolatility(), is(closeTo(0.1933, 0.011)));
        assertThat(putV.getVolatility(), is(closeTo(0.1926, 0.01)));
    }

    @Test
    public void volatilityIsProperlyCalculated_whenGivenOptionPriceDuo() {
        final CallOption callV = new CallOption("IWM", 143.45, 165d, 1.57d, LocalDate.of(2019, 1, 16), LocalDate.of(2019, 9, 20), 0.015);
        assertThat(callV.getVolatility(), is(closeTo(0.1569, 0.011)));
    }
}
