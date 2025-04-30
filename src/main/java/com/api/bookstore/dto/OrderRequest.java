package com.api.bookstore.dto;

public class OrderRequest {
    private int bookId;

    public OrderRequest() {
    }

    public OrderRequest(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
} 