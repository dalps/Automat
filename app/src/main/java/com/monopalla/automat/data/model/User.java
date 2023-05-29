package com.monopalla.automat.data.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public void addFavorite(Product product) {
        favoriteProducts.add(product);
    }

    public void removeFavorite(Product product) {
        favoriteProducts.remove(product);
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

    public void addToHistory(Order order) {
        orderHistory.add(order);
    }

    public int historyLength() {
        return orderHistory.size();
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

    public Set<Product> getFavoriteProducts() {
        return favoriteProducts;
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
