package com.assets.data.loader.impl;

import com.assets.data.loader.DataLoader;
import com.assets.data.loader.exceptions.DataLoaderEmptyFileException;
import com.assets.data.loader.exceptions.DataLoaderFileNotFoundException;
import com.assets.data.loader.exceptions.DataLoaderIOException;
import com.assets.entities.StockPrice;
import com.assets.statistic.list.StockList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoaderCsv implements DataLoader {
    
    //TODO Use properties to format date
    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    private static final String READ_CSV_SEPARATOR = ",";
    
    private final Logger logger = LogManager.getLogger(DataLoaderCsv.class);
    private final SimpleDateFormat dateFormatter;
    
    private String dataFile;
    private Map<String, StockList> data;
    
    public DataLoaderCsv() {
        dateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        data = new HashMap<String, StockList>();
    }
    
    public DataLoaderCsv(String dataFile){
        this();
        setDataFile(dataFile);
    }
    
    public Map<String, StockList> loadData() {
        return loadData(0);
    }
    
    public Map<String, StockList> loadData(Integer amount) {
        if(dataFile == null){
            throw new DataLoaderEmptyFileException();
        }
        File directory = new File(dataFile);
        
        if(amount.equals(0)){
            if(directory.listFiles() == null){
                amount = 1;
            }else{
                amount = directory.listFiles().length;
            }
        }
        for(int i = 0; i < amount; i++){
            File file;
            if(directory.isDirectory()) {
                file = directory.listFiles()[i];
            }else{
                file = directory;
            }
            String ticker = file.getName().split("_")[1].split("\\.")[0].toUpperCase();
            try {
                data.put(ticker, loadList(ticker, file));
            }catch(DataLoaderFileNotFoundException de){
                logger.error(String.format("The file with ticker %s not found", ticker));
            }catch(DataLoaderIOException dle){
                logger.error(String.format("An IO error occurred while reading file %s", ticker));
            }
        }
        
        return data;
    }
    
    private StockList loadList(String ticker, File file) {
        StockList prices = new StockList(ticker);
        for(String[] value : readCsv(file)){
            try {
                prices.add(new StockPrice(ticker, Instant.ofEpochMilli(dateFormatter.parse(value[0]).getTime()), new BigDecimal(value[2])));
            } catch (ParseException e) {
                logger.error(String.format("Error parsing the date %s with ticker %s", value[0], ticker), e);
            }
        }
        return prices;
    }

    private List<String[]> readCsv(File file) {
        List<String[]> list = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new DataLoaderFileNotFoundException();
        }
        try {
            String line = br.readLine();
            while (line != null) {
                list.add(line.split(READ_CSV_SEPARATOR));
                line = br.readLine();
            }
            br.close();
        }catch(IOException io){
            throw new DataLoaderIOException();
        }
        return list;
    }
    
    @Override
    public StockList loadData(String ticker) {
        if (data.isEmpty()) {
            loadData();
        }
        return data.get(ticker);
    }

//    @Override
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

}
