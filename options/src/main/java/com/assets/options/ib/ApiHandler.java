package com.assets.options.ib;

import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.List;

public class ApiHandler implements ApiController.IConnectionHandler {
    @Override
    public void connected() {
        System.out.println("Connected");
    }

    @Override
    public void disconnected() {
        System.out.println("Disconnected");
    }

    @Override
    public void accountList(List<String> list) {
        list.forEach(System.out::println);
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        System.out.println(String.format("message:[%s, %s, %s]", id, errorCode, errorMsg));
    }

    @Override
    public void show(String string) {
        System.out.println(string);
    }
}
