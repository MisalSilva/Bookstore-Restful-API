package com.api.bookstore.resource;

import com.api.bookstore.datastore.CustomerData;
import com.api.bookstore.datastore.OrderData;
import com.api.bookstore.datastore.BookData;
import com.api.bookstore.datastore.CartData;
import com.api.bookstore.exception.CustomerNotFoundException;
import com.api.bookstore.exception.OrderNotFoundException;
import com.api.bookstore.exception.InvalidInputException;
import com.api.bookstore.exception.BookNotFoundException;
import com.api.bookstore.model.Order;
import com.api.bookstore.model.OrderItem;
import com.api.bookstore.model.Book;
import com.api.bookstore.model.CartItem;
import com.api.bookstore.dto.OrderRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    // POST /customers/{customerId}/orders
    @POST
    public Response createOrder(@PathParam("customerId") int customerId, OrderRequest request) {
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }

        // Check if book exists
        Book book = BookData.findBookById(request.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + request.getBookId() + " not found");
        }

        // Get cart item for this book
        CartItem cartItem = CartData.getCartItem(customerId, request.getBookId());
        if (cartItem == null) {
            throw new InvalidInputException("Book with ID " + request.getBookId() + " is not in the customer's cart");
        }

        // Create order item from cart item and book
        OrderItem orderItem = new OrderItem(cartItem, book.getPrice());

        // Create order with the single item
        Order order = new Order();
        order.setCustomerId(customerId);
        order.addItem(orderItem);

        // Create the order in the system
        Order createdOrder = OrderData.createOrder(order);

        // Remove the item from cart after successful order creation
        CartData.removeFromCart(customerId, request.getBookId());

        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    // GET /customers/{customerId}/orders
    @GET
    public Response getOrdersByCustomerId(@PathParam("customerId") int customerId) {
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }

        List<Order> orders = OrderData.getOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }

    // GET /customers/{customerId}/orders/{orderId}
    @GET
    @Path("/{orderId}")
    public Response getOrderById(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {

        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }

        Order order = OrderData.getOrderById(orderId);
        if (order == null || order.getCustomerId() != customerId) {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found for customer " + customerId);
        }

        return Response.ok(order).build();
    }
}

