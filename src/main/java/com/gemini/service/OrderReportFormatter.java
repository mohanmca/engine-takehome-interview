package com.gemini.service;

import com.gemini.model.Entities.Order;

import java.util.List;

public enum OrderReportFormatter {
    INSTANCE;

    public String format(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        for (Order o : orders) {
            sb.append(toString(o));
        }
        return sb.toString();
    }

    private String toString(Order buyOrder) {
      return buyOrder.parentOrderId() + " " +
              buyOrder.side().name() + " " +
              buyOrder.instrument() + " " +
              buyOrder.quantity() + " " +
              buyOrder.price() + System.lineSeparator();
    }
}
