package com.gemini.listener;

import com.gemini.model.Entities.Order;

public class TradeListenerPrinter implements TradeListener {
    @Override
    public String id() {
        return "default";
    }

    @Override
    public void onMatch(Order buyOrders, Order sellOrders) {
        StringBuffer sb = tradeReport(buyOrders, sellOrders);
        System.err.println(sb.toString());
    }

    private StringBuffer tradeReport(Order buyOrder, Order sellOrder) {
        StringBuffer sb = new StringBuffer();
        sb.append("TRADE ");
        sb.append(buyOrder.instrument()).append(" ");
        sb.append(buyOrder.parentOrderId()).append(" ");
        sb.append(sellOrder.parentOrderId()).append(" ");
        sb.append(sellOrder.quantity()).append(" ");
        sb.append(buyOrder.price()).append(" ");
        return sb;
    }


}
