/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.bookstore.model;

/**
 *
 * @author misal
 */
public class OrderItem {
    private int bookId;
    private int quantity;
    private double price; // Price at time of order

    // Default constructor
    public OrderItem() {
    }

    // Constructor with all fields
    public OrderItem(int bookId, int quantity, double price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    // Constructor to create from CartItem and Book
    public OrderItem(CartItem cartItem, double bookPrice) {
        this.bookId = cartItem.getBookId();
        this.quantity = cartItem.getQuantity();
        this.price = bookPrice;
    }

    // Getters and setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Calculate subtotal for this item
    public double getSubtotal() {
        return price * quantity;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "OrderItem{" +
                "bookId=" + bookId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + getSubtotal() +
                '}';
    }
    
}
