package com.api.bookstore.resource;

import com.api.bookstore.datastore.CustomerData;
import com.api.bookstore.exception.CustomerNotFoundException;
import com.api.bookstore.model.Customer;
import com.api.bookstore.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @GET
    public List<Customer> getAllCustomers() {
        return CustomerData.getAllCustomers();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") int id) {
        Customer customer = CustomerData.findCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        return customer;
    }

//    @GET
//    @Path("/email/{email}")
//    public Customer getCustomerByEmail(@PathParam("email") String email) {
//        Customer customer = CustomerData.findCustomerByEmail(email);
//        if (customer == null) {
//            throw new CustomerNotFoundException("Customer with email " + email + " not found");
//        }
//        return customer;
//    }

    @POST
    public Response addCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email is required");
        }
        Customer existingCustomer = CustomerData.findCustomerByEmail(customer.getEmail());
        if (existingCustomer != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Customer with email " + customer.getEmail() + " already exists")
                    .build();
        }
        
        Customer addedCustomer = CustomerData.addCustomer(customer);
        return Response.status(Response.Status.CREATED)
                .entity(addedCustomer)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email is required");
        }
        Customer existingCustomer = CustomerData.findCustomerById(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        
        customer.setId(id);
        Customer updatedCustomer = CustomerData.updateCustomer(customer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer existingCustomer = CustomerData.findCustomerById(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        
        CustomerData.deleteCustomer(id);
        return Response.noContent().build();
    }
}