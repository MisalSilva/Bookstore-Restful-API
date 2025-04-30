package com.api.bookstore.datastore;

import com.api.bookstore.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerData {
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // --- Customer methods ---
    public static Customer addCustomer(Customer customer) {
        int id = idGenerator.getAndIncrement();
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    public static Customer findCustomerById(int id) {
        return customers.get(id);
    }

    public static Customer findCustomerByEmail(String email) {
        return customers.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public static Customer updateCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public static void deleteCustomer(int id) {
        customers.remove(id);
    }
}