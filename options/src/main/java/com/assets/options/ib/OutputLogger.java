package com.assets.options.ib;

import com.ib.controller.ApiConnection;

public class OutputLogger implements ApiConnection.ILogger {
    @Override
    public void log(String valueOf) {
        System.out.println(String.format("Output logger: %s", valueOf));
    }
}
