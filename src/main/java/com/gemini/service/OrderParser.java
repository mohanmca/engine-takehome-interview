package com.gemini.service;


import java.util.Optional;

import static com.gemini.model.Entities.Order;
import static com.gemini.model.Entities.Side;

public class OrderParser {
    public static Optional<Order> parse(String order) {
        try {
            String orderString = order.replaceAll("\s+", " ").trim();
            String[] fields = orderString.split(" ");
            if (fields.length == 5) {
                Order o = new Order(fields[0], fields[0], Side.valueOf(fields[1]), fields[2], Integer.parseInt(fields[3]), Float.parseFloat(fields[4]), System.nanoTime(), System.nanoTime());
                return Optional.of(o);
            }
        } catch (IllegalArgumentException e) {
            System.err.printf("Error while parsing order `%s`, %s \n", order, e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

}
