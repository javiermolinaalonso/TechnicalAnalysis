package com.assets.options.ib;

import com.ib.controller.ApiConnection;

public class InputLogger implements ApiConnection.ILogger {
    @Override
    public void log(String valueOf) {
        System.out.println(String.format("Input logger: %s", valueOf));
    }
}
