package com.gemini;

import com.gemini.Entities.Order;
import com.gemini.Entities.OrderComparator;
import com.gemini.Entities.Side;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntitiesTest {

    @Test
    public void orderComparatorTest() {
        List<Order> orders = new LinkedList<>(Arrays.asList(
                new Order("1", Side.BUY, "apple", 1, 2.5f, 4, 5),
                new Order("1", Side.BUY, "apple", 1, 2.5f, 4, 3)));
        OrderComparator comparator = new OrderComparator();
        orders.sort(comparator);

        assertEquals(orders.get(0).arrivedTime(), 3, "When there is price collision, sort based on arrival time");
        orders.add(new Order("1", Side.BUY, "apple", 1, 1.5f, 4, 4));

        orders.sort(comparator);
        assertEquals(orders.get(0).price(), 1.5f, 0.0001, "Lowest price as fist order");

    }


}