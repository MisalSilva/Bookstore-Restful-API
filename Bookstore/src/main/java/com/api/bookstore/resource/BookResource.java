package com.api.bookstore.resource;

import com.api.bookstore.datastore.BookData;
import com.api.bookstore.datastore.AuthorData;
import com.api.bookstore.exception.AuthorNotFoundException;
import com.api.bookstore.exception.BookNotFoundException;
import com.api.bookstore.model.Book;
import com.api.bookstore.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @GET
    public List<Book> getAllBooks() {
        return BookData.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Book getBookById(@PathParam("id") int id) {
        Book book = BookData.findBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        return book;
    }

    @POST
    public Response addBook(Book book) {
        if (book == null) {
            throw new InvalidInputException("Book data cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        if (book.getAuthorId() <= 0) {
            throw new InvalidInputException("Invalid author ID");
        }
        if (book.getPrice() < 0) {
            throw new InvalidInputException("Book price cannot be negative");
        }
        if (AuthorData.findAuthorById(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " not found");
        }
        Book addedBook = BookData.addBook(book);
        return Response.status(Response.Status.CREATED)
                .entity(addedBook)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book book) {
        if (book == null) {
            throw new InvalidInputException("Book data cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        if (book.getAuthorId() <= 0) {
            throw new InvalidInputException("Invalid author ID");
        }
        if (book.getPrice() < 0) {
            throw new InvalidInputException("Book price cannot be negative");
        }
        Book existingBook = BookData.findBookById(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Cannot update: Book with ID " + id + " not found");
        }
        if (AuthorData.findAuthorById(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " not found");
        }
        book.setId(id);
        return BookData.updateBook(book);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book existingBook = BookData.findBookById(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Cannot delete: Book with ID " + id + " not found");
        }
        BookData.deleteBook(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}