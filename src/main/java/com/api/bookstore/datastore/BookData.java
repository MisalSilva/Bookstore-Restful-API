package com.api.bookstore.datastore;

import com.api.bookstore.exception.InvalidInputException;
import com.api.bookstore.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookData {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // --- Book methods ---
    public static Book addBook(Book book) {
        int id = idGenerator.getAndIncrement();
        book.setId(id);
        books.put(id, book);
        return book;
    }

    public static Book findBookById(int id) {
        return books.get(id);
    }

    public static List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public static Book updateBook(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    public static void deleteBook(int id) {
        books.remove(id);
    }

    public static void updateStock(int bookId, int newStock) {
        Book book = books.get(bookId);
        if (book == null) {
            throw new InvalidInputException("Cannot update stock: book with ID " + bookId + " does not exist.");
        }
        if (newStock < 0) {
            throw new InvalidInputException("Cannot update stock: stock cannot be negative for book ID " + bookId + ".");
        }
        book.setStock(newStock);
        books.put(bookId, book);
    }
    
    public static List<Book> getBooksByAuthorId(int authorId) {
        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == authorId) {
                booksByAuthor.add(book);
            }
        }
        return booksByAuthor;
    }

}