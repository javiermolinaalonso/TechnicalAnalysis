package com.assets.options.ib;

import com.ib.client.Contract;
import com.ib.contracts.OptContract;
import com.ib.controller.*;

import java.util.ArrayList;

public class IBMain {

    public static void main(String[] args) throws InterruptedException {
        ApiController.IConnectionHandler handler = new ApiController.IConnectionHandler() {
            @Override
            public void connected() {
                System.out.println("Connected");
            }

            @Override
            public void disconnected() {
                System.out.println("Disconnected");
            }

            @Override
            public void accountList(ArrayList<String> arrayList) {
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void message(int id, int errorCode, String errorMsg) {
                System.out.println(errorCode);
                System.out.println(errorMsg);
            }

            @Override
            public void show(String string) {
                System.out.println(string);
            }
        };
        ApiConnection.ILogger logger = new ApiConnection.ILogger() {
            @Override
            public void log(String valueOf) {
                System.out.print(valueOf);
            }
        };
        ApiConnection.ILogger outlogger = new ApiConnection.ILogger() {
            @Override
            public void log(String valueOf) {
                System.out.print(valueOf);
            }
        };
        final ApiController client = new ApiController(handler, logger, outlogger);

        client.connect("localhost", 7497, 0);

        Contract contract = new OptContract("SPY", "20190225", 280, "C");
//        Contract contract = new Contract(0, "SPY", "OPT", "20180225", 280d, "C", "100", "SMART", "USD", "SPY", null, null, null, false, null, null);
        NewContract newContract = new NewContract(contract);
        ApiController.IContractDetailsHandler processor = list -> list.forEach(System.out::println);
//        client.reqContractDetails(newContract, processor);
        ApiController.IOptHandler iopth = new ApiController.IOptHandler() {
            @Override
            public void tickOptionComputation(NewTickType newTickType, double v, double v1, double v2, double v3, double v4, double v5, double v6, double v7) {

            }

            @Override
            public void tickPrice(NewTickType newTickType, double v, int i) {

            }

            @Override
            public void tickSize(NewTickType newTickType, int i) {

            }

            @Override
            public void tickString(NewTickType newTickType, String s) {

            }

            @Override
            public void tickSnapshotEnd() {

            }

            @Override
            public void marketDataType(Types.MktDataType mktDataType) {

            }
        };

        client.reqOptionMktData(newContract, "100,106", false, iopth);
        Thread.sleep(1000);
//        System.exit(0);
    }
}
