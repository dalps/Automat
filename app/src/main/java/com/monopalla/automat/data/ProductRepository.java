package com.monopalla.automat.data;

import com.monopalla.automat.data.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private static ProductRepository instance;
    private final Map<String, Product> products = new HashMap<>();

    private ProductRepository() {
        products.put("Fiesta", new Product("Fiesta","", "Snack", 1.00f, null));
        products.put("Oreo", new Product("Oreo","", "Snack", 1.50f, null));
        products.put("Fonzies", new Product("Fonzies","", "Snack", 0.70f, null));
        products.put("Tuc", new Product("Tuc","", "Snack", 0.50f, null));
    }

    public static ProductRepository getInstance() {
        if(instance == null) {
            instance = new ProductRepository();
        }

        return instance;
    }

    public Product getProduct(String name) {
        return products.get(name);
    }
}
