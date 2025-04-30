package com.api.bookstore.resource;

import com.api.bookstore.datastore.CartData;
import com.api.bookstore.datastore.BookData;
import com.api.bookstore.exception.BookNotFoundException;
import com.api.bookstore.exception.CartNotFoundException;
import com.api.bookstore.exception.InvalidInputException;
import com.api.bookstore.model.Cart;
import com.api.bookstore.model.CartItem;
import com.api.bookstore.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @PathParam("customerId")
    private int customerId;

    @GET
    public Response getCart() {
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }
        return Response.ok(cart).build();
    }

    @POST
    @Path("/items")
    public Response addItemToCart(CartItem item) {
        if (item == null) {
            throw new InvalidInputException("Cart item cannot be null");
        }

        // Validate that the book exists and check stock
        Book book = BookData.findBookById(item.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Cannot add item: Book with ID " + item.getBookId() + " not found");
        }

        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            // Create a new cart for the customer
            cart = new Cart(customerId, new ArrayList<>());
            CartData.addCart(cart);
        }

        List<CartItem> items = cart.getItems();
        boolean itemExists = false;
        int currentQuantity = 0;

        // First, find if the item exists and get current quantity
        for (CartItem cartItem : items) {
            if (cartItem.getBookId() == item.getBookId()) {
                currentQuantity = cartItem.getQuantity();
                itemExists = true;
                break;
            }
        }

        // Calculate remaining stock
        int remainingStock = book.getStock() - currentQuantity;

        // Validate if new quantity exceeds remaining stock
        if (item.getQuantity() > remainingStock) {
            throw new InvalidInputException("Cannot add item: Requested quantity (" + item.getQuantity() + 
                ") exceeds remaining stock (" + remainingStock + ") for book ID " + item.getBookId());
        }

        // Now update the cart and stock
        if (itemExists) {
            for (CartItem cartItem : items) {
                if (cartItem.getBookId() == item.getBookId()) {
                    cartItem.setQuantity(currentQuantity + item.getQuantity());
                    break;
                }
            }
        } else {
            items.add(item);
        }

        // Update book stock
        BookData.updateStock(item.getBookId(), remainingStock - item.getQuantity());

        cart.setItems(items);
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("bookId") int bookId, CartItem item) {
        if (item == null) {
            throw new InvalidInputException("Cart item cannot be null");
        }

        // Validate that the book exists and check stock
        Book book = BookData.findBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Cannot update item: Book with ID " + bookId + " not found");
        }

        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cannot update item: Cart not found for customer ID: " + customerId);
        }

        // Find the current quantity in cart
        int currentQuantity = 0;
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getBookId() == bookId) {
                currentQuantity = cartItem.getQuantity();
                break;
            }
        }

        // Calculate the difference in quantity
        int quantityDifference = item.getQuantity() - currentQuantity;
        
        // Check if the new total stock would be sufficient
        if (quantityDifference > 0 && quantityDifference > book.getStock()) {
            throw new InvalidInputException("Cannot update item: Requested quantity increase (" + quantityDifference + 
                ") exceeds available stock (" + book.getStock() + ") for book ID " + bookId);
        }

        // Update book stock
        BookData.updateStock(bookId, book.getStock() - quantityDifference);

        boolean updated = false;
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getBookId() == bookId) {
                cartItem.setQuantity(item.getQuantity());
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new InvalidInputException("Book with ID " + bookId + " not found in cart");
        }

        return Response.ok(cart).build();
    }

    @DELETE
    public Response deleteCart() {
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cannot delete: Cart not found for customer ID: " + customerId);
        }
        CartData.deleteCart(customerId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("bookId") int bookId) {
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cannot remove item: Cart not found for customer ID: " + customerId);
        }

        // Find the item to be removed
        CartItem itemToRemove = null;
        for (CartItem item : cart.getItems()) {
            if (item.getBookId() == bookId) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove == null) {
            throw new InvalidInputException("Book with ID " + bookId + " not found in cart");
        }

        // Update book stock by adding back the removed quantity
        Book book = BookData.findBookById(bookId);
        if (book != null) {
            BookData.updateStock(bookId, book.getStock() + itemToRemove.getQuantity());
        }

        // Remove the item from cart
        cart.getItems().removeIf(item -> item.getBookId() == bookId);
        return Response.ok(cart).build();
    }
}
