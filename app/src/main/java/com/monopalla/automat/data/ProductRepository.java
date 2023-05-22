package com.monopalla.automat.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private static ProductRepository instance;
    private final Map<String, Product> products = new HashMap<>();
    private final Context context;
    private Product currentProduct;
    private final ArrayList<Product> cart;
    public static final int CART_SIZE = 5;

    private ProductRepository(Context context) {
        this.context = context;

        products.put("Fiesta", new Product("Fiesta","", "Snack", 1.00f, toBitmap(R.drawable.fiesta)));
        products.put("Oreo", new Product("Oreo","", "Snack", 1.50f, toBitmap(R.drawable.oreo)));
        products.put("Fonzies", new Product("Fonzies","", "Snack", 0.70f, toBitmap(R.drawable.fonzies)));
        products.put("Tuc", new Product("Tuc","", "Snack", 0.50f, toBitmap(R.drawable.tuc)));
        products.put("Crackers", new Product("Crackers", "", "Snack", 0.60f, toBitmap(R.drawable.crackers)));
        products.put("Mars", new Product("Mars", "", "Snack", 0.70f, toBitmap(R.drawable.mars)));
        products.put("Croissant al Cioccolato", new Product("Croissant al Cioccolato", "", "Snack", 1.00f, toBitmap(R.drawable.chocroissant)));
        products.put("Acqua Naturale", new Product("Acqua Naturale", "", "Bevanda", 0.70f, toBitmap(R.drawable.naturale)));
        products.put("Acqua Frizzante", new Product("Acqua Frizzante", "", "Bevanda", 0.70f, toBitmap(R.drawable.frizzante)));
        products.put("Succo alla Pera", new Product("Succo alla Pera", "", "Bevanda", 0.50f, toBitmap(R.drawable.pearjuice)));
        products.put("Coca Cola", new Product("Coca Cola", "", "Bevanda", 0.90f, toBitmap(R.drawable.cocacola)));
        products.put("The alla Pesca", new Product("Thè alla Pesca", "", "Bevanda", 0.90f, toBitmap(R.drawable.pesca)));

        cart = new ArrayList<>();
    }

    public static ProductRepository getInstance(Context context) {
        if(instance == null) {
            instance = new ProductRepository(context);
        }

        return instance;
    }

    public Product getProduct(String name) {
        return products.get(name);
    }

    public Bitmap toBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void addToCart(Product product) {
        cart.add(product);
    }

    public void clearCart() {
        cart.clear();
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public boolean isCartFull() {
        return cart.size() >= CART_SIZE;
    }

    public void removeFromCart(Product product) {
        cart.remove(product);
    }
}
