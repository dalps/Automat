package com.monopalla.automat.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Comparable<Product> {
    private String name; // A unique identifier for the product
    private String description;
    private String type;
    private float price;
    private Bitmap picture;

    public Product(String name, String description, String type, float price, Bitmap picture) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(Product product) {
        return this.getName().compareTo(product.getName());
    }
}
