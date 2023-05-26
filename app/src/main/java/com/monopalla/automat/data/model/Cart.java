package com.monopalla.automat.data.model;

import java.util.ArrayList;
import java.util.Objects;

public class Cart {
    final ArrayList<CartItem> items;
    int capacity;

    public Cart(int size) {
        items = new ArrayList<>();
        this.capacity = size;
    }

    public void insertProductUnit(Product product) {
        if (items.size() < capacity) {
            items.add(new CartItem(product, 1));
        }
    }

    public boolean increaseItemQuanitity(int index) {
        if (!isCartFull()) {
            items.get(index).add();
        }

        return false;
    }

    public boolean decreaseItemQuanitity(int index) {
        if (!isCartEmpty()) {
            items.get(index).remove();
        }

        return false;
    }

    public void removeProduct(Product product) {
        items.removeIf(cartItem -> cartItem.getProduct().equals(product));
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public CartItem getItem(int index) {
        return items.get(index);
    }

    public void clearCart() {
        items.clear();
    }

    public boolean isCartEmpty() {
        return items.isEmpty();
    }

    public boolean isCartFull() {
        return items.stream().mapToInt(CartItem::getQuantity).sum() >= capacity;
    }

    public double getTotal() {
        return getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public static class CartItem {
        Product product;
        int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void add() {
            quantity += 1;
        }

        public void remove() {
            if (quantity > 1) quantity -= 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CartItem)) return false;
            CartItem cartItem = (CartItem) o;
            return getProduct().equals(cartItem.getProduct());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getProduct());
        }
    }
}
