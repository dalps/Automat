package com.monopalla.automat.data.model;

import android.graphics.Bitmap;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class User {
    String username;
    String password;
    String name;
    Bitmap profilePicture;
    final HashSet<Product> favoriteProducts;

    final ArrayList<Order> orderHistory;
    int automats;

    public User(String username, String password, String name, Bitmap profilePicture) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
        favoriteProducts = new HashSet<>();
        orderHistory = new ArrayList<>();
        automats = 0;
    }

    public User(String username, String password, String name, Bitmap profilePicture, int automats) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
        favoriteProducts = new HashSet<>();
        orderHistory = new ArrayList<>();
        this.automats = automats;
    }

    public static class MarkedFavoriteEvent {
        public final Product product;

        public MarkedFavoriteEvent(Product product) {
            this.product = product;
        }
    }

    public static class UnmarkedFavoriteEvent {
        public final Product product;

        public UnmarkedFavoriteEvent(Product product) {
            this.product = product;
        }
    }

    public void addFavorite(Product product) {
        favoriteProducts.add(product);

        EventBus.getDefault().post(new MarkedFavoriteEvent(product));
    }

    public void removeFavorite(Product product) {
        favoriteProducts.remove(product);

        EventBus.getDefault().post(new UnmarkedFavoriteEvent(product));
    }

    public boolean isProductFavorite(Product product) {
        return favoriteProducts.contains(product);
    }

    public boolean anyFavorites() {
        return !favoriteProducts.isEmpty();
    }

    public void addAutomats(int gain) {
        automats += gain;
    }

    public void spendAutomats(int loss) {
        automats = Math.max(0, automats-loss);
    }

    public int getAutomats() {
        return automats;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addToHistory(Order order) {
        orderHistory.add(order);
    }

    public int historyLength() {
        return orderHistory.size();
    }

    public boolean anyOrders() {
        return !orderHistory.isEmpty();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ArrayList<Product> getFavoriteProducts() {
        return favoriteProducts.stream()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
