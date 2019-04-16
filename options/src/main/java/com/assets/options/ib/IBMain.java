package com.assets.options.ib;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.contracts.OptContract;
import com.ib.controller.*;

import java.util.ArrayList;
import java.util.List;

public class IBMain {

    public static void main(String[] args) throws InterruptedException {
        final ApiController controller = new ApiController(new ApiHandler(), new InputLogger(), new OutputLogger());

        controller.connect("localhost", 7496, 8, null);

        Contract contract = new Contract();
        contract.symbol("FISV");
        contract.secType("OPT");
        contract.currency("USD");
        contract.exchange("SMART");

        ApiController.IContractDetailsHandler processor = list -> list.forEach(System.out::println);
        controller.reqContractDetails(contract, processor);
        controller.disconnect();
    }
}
