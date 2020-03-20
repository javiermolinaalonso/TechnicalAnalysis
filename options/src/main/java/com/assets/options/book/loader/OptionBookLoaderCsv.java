package com.assets.options.book.loader;

import com.assets.data.loader.impl.AbstractCsvLoader;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionBuilder;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class OptionBookLoaderCsv extends AbstractCsvLoader<Option> {

    private static final String CALL = "C";
    private static final String PUT = "P";
    private final SimpleDateFormat dateFormat;
    private final LocalDate now;

    public OptionBookLoaderCsv(LocalDate now) {
        this(new SimpleDateFormat("yyyyMMdd"), now);
    }

    public OptionBookLoaderCsv(SimpleDateFormat dateFormat, LocalDate now) {
        this.dateFormat = dateFormat;
        this.now = now;
    }

    /*
        The format of the HistData is
        Date,Open,High,Low,Close,Volume,Adj Close
        Underlying	UnderlyingPrice	Expiry	Type	Strike	Last	Bid	Ask	Volume	OpenInterest
     */

    @Override
    protected Option parseLine(String[] data) {
        String callOrPut = data[3];

        try {
            Date day = dateFormat.parse(data[2]);
            LocalDate expiry = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            OptionBuilder optionBuilder = OptionBuilder.create(data[0], new BigDecimal(data[1]).doubleValue())
                    .withStrikePrice(new BigDecimal(data[4]).doubleValue())
                    .withBidAsk(new BigDecimal(data[6]).doubleValue(), new BigDecimal(data[7]).doubleValue())
                    .withCurrentDate(now)
                    .withExpirationAt(expiry);
            if(CALL.equals(callOrPut)) {
                return optionBuilder.buildCall();
            } else {
                return optionBuilder.buildPut();
            }
        } catch (ParseException e) {
            return null;
        }
    }

}
