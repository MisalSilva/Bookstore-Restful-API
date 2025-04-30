/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.bookstore.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author misal
 */
public class Order {
    private int id;
    private int customerId;
    private Date orderDate;
    private String status;
    private double totalAmount;
    private List<OrderItem> items;

    // Default constructor
    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = new Date(); // Default to current date
        this.status = "PENDING"; // Default status
    }

    // Constructor with all fields
    public Order(int id, int customerId, Date orderDate, String status, double totalAmount, List<OrderItem> items) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        // Defensive copy to prevent external modification
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    // Constructor for creating order from cart
    public Order(int id, int customerId, List<OrderItem> items) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = new Date();
        this.status = "PENDING";
        // Defensive copy to prevent external modification
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.calculateTotalAmount();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        // Return a defensive copy to prevent external modification
        return new ArrayList<>(items);
    }

    public void setItems(List<OrderItem> items) {
        // Defensive copy to prevent external modification
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        calculateTotalAmount();
    }

    // Method to calculate total amount based on order items
    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Helper method to add an item to the order
    public void addItem(OrderItem item) {
        if (item != null) {
            items.add(item);
            calculateTotalAmount();
        }
    }

    // toString for debugging
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                '}';
    }
    
}
