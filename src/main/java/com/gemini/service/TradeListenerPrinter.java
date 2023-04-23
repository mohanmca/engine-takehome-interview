package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.List;

public class TradeListenerPrinter implements TradeListener {
  @Override
  public String id() {
    return "default";
  }

  @Override
  public void onMatch(Order buyOrders, Order sellOrders) {
    StringBuffer sb = tradeReport(buyOrders, sellOrders);
    System.out.print(sb);
  }

  private StringBuffer tradeReport(Order buyOrder, Order sellOrder) {
    StringBuffer sb = new StringBuffer();
    sb.append("TRADE ");
    sb.append(buyOrder.instrument()).append(" ");
    sb.append(buyOrder.parentOrderId()).append(" ");
    sb.append(sellOrder.parentOrderId()).append(" ");
    sb.append(sellOrder.quantity()).append(" ");
    sb.append(buyOrder.price()).append(" ");
    sb.append("\n");
    return sb;
  }

  public StringBuffer tradeReport(List<Order> orders) {
    StringBuffer sb = new StringBuffer();
    for (Order o : orders) {
      sb.append(toString(o));
    }
    return sb.toString();
  }

  private String toString(Order buyOrder) {
    StringBuffer sb = new StringBuffer();
    sb.append(buyOrder.parentOrderId()).append(" ");
    sb.append(buyOrder.side().name()).append(" ");
    sb.append(buyOrder.quantity()).append(" ");
    sb.append(buyOrder.price()).append(System.lineSeparator());
    return sb.toString();
  }
}
