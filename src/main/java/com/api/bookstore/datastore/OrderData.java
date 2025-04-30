 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.bookstore.datastore;

import com.api.bookstore.exception.InvalidInputException;
import com.api.bookstore.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author misal
 */
public class OrderData {
    private static final Map<Integer, Order> orders = new HashMap<>();
    private static int nextId = 1;

    public static Order createOrder(Order order) {
        if (order == null) {
            throw new InvalidInputException("Order cannot be null");
        }

        order.setId(nextId++);
        orders.put(order.getId(), order);
        return order;
    }

    public static Order getOrderById(int id) {
        return orders.get(id);
    }

    public static List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId() == customerId) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public static void deleteOrder(int id) {
        orders.remove(id);
    }

    public static List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}
