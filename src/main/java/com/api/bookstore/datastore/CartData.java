package com.api.bookstore.datastore;

import com.api.bookstore.exception.InvalidInputException;
import com.api.bookstore.model.Cart;
import com.api.bookstore.model.CartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartData {
    private static final Map<Integer, Cart> carts = new HashMap<>();

    public static Cart addCart(Cart cart) {
        if (CustomerData.findCustomerById(cart.getCustomerId()) == null) {
            throw new InvalidInputException("Cannot create cart: customer with ID " + cart.getCustomerId() + " does not exist.");
        }
        carts.put(cart.getCustomerId(), cart);
        return cart;
    }

    public static Cart findCartByCustomerId(int customerId) {
        return carts.get(customerId);
    }

    public static void deleteCart(int customerId) {
        carts.remove(customerId);
    }

    public static void updateCartItems(int customerId, List<CartItem> items) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.setItems(items);
        }
    }

    public static boolean updateCartItemQuantity(int customerId, int bookId, int quantity) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getBookId() == bookId) {
                    item.setQuantity(quantity);
                    return true;
                }
            }
        }
        return false;
    }

    public static CartItem getCartItem(int customerId, int bookId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getBookId() == bookId) {
                    return item;
                }
            }
        }
        return null;
    }

    public static void removeFromCart(int customerId, int bookId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            List<CartItem> items = cart.getItems();
            items.removeIf(item -> item.getBookId() == bookId);
            cart.setItems(items);
        }
    }
}
