/*
 * This file is generated by jOOQ.
*/
package com.assets.data.loader.mysql.entities.tables.records;


import com.assets.data.loader.mysql.entities.tables.DailyQuotes;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Date;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DailyQuotesRecord extends UpdatableRecordImpl<DailyQuotesRecord> implements Record4<Integer, String, Date, Double> {

    private static final long serialVersionUID = -1046891974;

    /**
     * Setter for <code>portfolio.daily_quotes.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>portfolio.daily_quotes.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>portfolio.daily_quotes.ticker</code>.
     */
    public void setTicker(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>portfolio.daily_quotes.ticker</code>.
     */
    public String getTicker() {
        return (String) get(1);
    }

    /**
     * Setter for <code>portfolio.daily_quotes.date</code>.
     */
    public void setDate(Date value) {
        set(2, value);
    }

    /**
     * Getter for <code>portfolio.daily_quotes.date</code>.
     */
    public Date getDate() {
        return (Date) get(2);
    }

    /**
     * Setter for <code>portfolio.daily_quotes.value</code>.
     */
    public void setValue(Double value) {
        set(3, value);
    }

    /**
     * Getter for <code>portfolio.daily_quotes.value</code>.
     */
    public Double getValue() {
        return (Double) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, String, Date, Double> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, String, Date, Double> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return DailyQuotes.DAILY_QUOTES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return DailyQuotes.DAILY_QUOTES.TICKER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field3() {
        return DailyQuotes.DAILY_QUOTES.DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field4() {
        return DailyQuotes.DAILY_QUOTES.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getTicker();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value3() {
        return getDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value4() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DailyQuotesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DailyQuotesRecord value2(String value) {
        setTicker(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DailyQuotesRecord value3(Date value) {
        setDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DailyQuotesRecord value4(Double value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DailyQuotesRecord values(Integer value1, String value2, Date value3, Double value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DailyQuotesRecord
     */
    public DailyQuotesRecord() {
        super(DailyQuotes.DAILY_QUOTES);
    }

    /**
     * Create a detached, initialised DailyQuotesRecord
     */
    public DailyQuotesRecord(Integer id, String ticker, Date date, Double value) {
        super(DailyQuotes.DAILY_QUOTES);

        set(0, id);
        set(1, ticker);
        set(2, date);
        set(3, value);
    }
}