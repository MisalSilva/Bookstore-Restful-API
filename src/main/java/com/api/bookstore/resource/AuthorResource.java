package com.api.bookstore.resource;

import com.api.bookstore.datastore.AuthorData;
import com.api.bookstore.exception.AuthorNotFoundException;
import com.api.bookstore.model.Author;
import com.api.bookstore.datastore.BookData;
import com.api.bookstore.model.Book;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import com.api.bookstore.exception.BookNotFoundException;
import com.api.bookstore.exception.InvalidInputException;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @GET
    public List<Author> getAllAuthors() {
        return AuthorData.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") int id) {
        Author author = AuthorData.findAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        return author;
    }

    @POST
    public Response createAuthor(Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }
        if (author.getBiography() == null || author.getBiography().trim().isEmpty()) {
            throw new InvalidInputException("Author biography is required");
        }
        
        Author createdAuthor = AuthorData.addAuthor(author);
        return Response.status(Response.Status.CREATED)
                .entity(createdAuthor)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required for update");
        }
        if (author.getBiography() == null || author.getBiography().trim().isEmpty()) {
            throw new InvalidInputException("Author biography is required for update");
        }
        Author existingAuthor = AuthorData.findAuthorById(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        author.setId(id);
        return AuthorData.updateAuthor(author);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author existingAuthor = AuthorData.findAuthorById(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        try {
            AuthorData.deleteAuthor(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
    
    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int authorId) {
        Author author = AuthorData.findAuthorById(authorId);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + authorId + " not found");
        }

        List<Book> books = BookData.getBooksByAuthorId(authorId);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found for author with ID " + authorId);
        }
        return books;
    }

}