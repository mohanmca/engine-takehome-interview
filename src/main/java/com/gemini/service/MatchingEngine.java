package com.gemini.service;

public class MatchingEngine {

    private static MatchingEngine engine = null;
//    private OrderBook book = new OrderBook();


    private MatchingEngine() {

    }

    public static MatchingEngine getInstance() {
        if (engine == null) {
            engine = new MatchingEngine();
        }
        return engine;
    }

    public void placeOrder(String s) {
        OrderParser.parse(s);
    }


}
