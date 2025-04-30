package com.api.bookstore;

import com.api.bookstore.resource.AuthorResource;
import com.api.bookstore.exception.mapper.AuthorNotFoundExceptionMapper;
import com.api.bookstore.resource.BookResource;
import com.api.bookstore.exception.mapper.BookNotFoundExceptionMapper;
import com.api.bookstore.resource.CartResource;
import com.api.bookstore.exception.mapper.CartNotFoundExceptionMapper;
import com.api.bookstore.resource.CustomerResource;
import com.api.bookstore.exception.mapper.CustomerNotFoundExceptionMapper;
import com.api.bookstore.resource.OrderResource;
import com.api.bookstore.exception.mapper.OrderNotFoundExceptionMapper;
import com.api.bookstore.exception.mapper.InvalidInputExceptionMapper;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("") 
public class AppConfig extends Application{
    @Override 
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>(); 
        classes.add(BookResource.class); 
        classes.add(BookNotFoundExceptionMapper.class);
        classes.add(AuthorResource.class);
        classes.add(AuthorNotFoundExceptionMapper.class);
        classes.add(CartResource.class);
        classes.add(CartNotFoundExceptionMapper.class);
        classes.add(CustomerResource.class);
        classes.add(CustomerNotFoundExceptionMapper.class);
        classes.add(OrderResource.class);
        classes.add(OrderNotFoundExceptionMapper.class);
        classes.add(InvalidInputExceptionMapper.class);
        return classes;    
    }
}
