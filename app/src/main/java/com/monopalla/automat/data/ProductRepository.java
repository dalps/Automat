package com.monopalla.automat.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Cart;
import com.monopalla.automat.data.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private static ProductRepository instance;
    private final Map<String, Product> products = new HashMap<>();
    private final Context context;
    private Product currentProduct;
    private Cart cart;
    public static final int CART_SIZE = 5;

    private ProductRepository(Context context) {
        this.context = context;

        products.put("Fiesta", new Product(
                "Fiesta",
                "Tortina all'arancia con liquore ricoperta al cacao magro. Marca Ferrero.\n\n" +
                          "Valori nutrizionali per pezzo (36g):\n" +
                          "163kcal, 7,6g grassi, 20,2g carboidrati, 1,4g proteine",
                "Snack",
                0.70f,
                toBitmap(R.drawable.fiesta))
        );

        products.put("Oreo", new Product(
                "Oreo",
                "Gli originali biscotti Oreo nel loro formato più piccolo e stuzzicante!\n" +
                        "La confezione contiene 6 biscotti.\n\n" +
                        "Valori nutrizionali per biscotto (11g):\n" +
                        "52kcal, 2,2g grassi, 7,5g carboidrati, 0,6g proteine",
                "Snack",
                1.00f,
                toBitmap(R.drawable.oreo))
        );

        products.put("Fonzies", new Product(
                "Fonzies",
                "Gli immancabili croccantini di mais al formaggio.\n\n" +
                        "Valori nutrizionali per porzione (40g):\n" +
                        "210kcal, 13g grassi, 20g carboidrati, 3,1g proteine",
                "Snack",
                0.70f,
                toBitmap(R.drawable.fonzies))
        );

        products.put("Tuc", new Product(
                "Tuc",
                "Forma ottagonale per un gusto sensazionale.\n\n" +
                        "Valori nutrizionali per 100g:\n" +
                        "461kcal, 18g grassi, 65g carboidrati, 8,3g proteine",
                "Snack",
                0.60f,
                toBitmap(R.drawable.tuc))
        );

        products.put("Crackers", new Product(
                "Crackers",
                "I classici crackers salati Gran Pavesi.\n\n" +
                        "Valori nutrizionali per porzione (32g):\n" +
                        "138kcal, 4,4g grassi, 20,7g carboidrati, 3,1g proteine",
                "Snack",
                0.50f,
                toBitmap(R.drawable.crackers))
        );

        products.put("Mars", new Product(
                "Mars",
                "All'interno ha uno strato di malto e uno di caramello ricoperto di cioccolato al latte.\n\n" +
                        "Valori nutrizionali per porzione (51g):\n" +
                        "229kcal, 8,5g grassi, 35,8g carboidrati, 2,1g proteine",
                "Snack",
                0.70f,
                toBitmap(R.drawable.mars))
        );

        products.put("Croissant al Cioccolato", new Product(
                "Croissant al Cioccolato",
                "Un cuore di pregiato cioccolato, per una crema golosa e dal sapore intenso.\n\n" +
                        "Valori nutrizionali per porzione (50g):\n" +
                        "210kcal, 11g grassi, 22,5g carboidrati, 3,2g proteine",
                "Snack",
                1.00f,
                toBitmap(R.drawable.chocroissant))
        );

        products.put("Acqua Naturale", new Product(
                "Acqua Naturale",
                "Acqua minerale naturale San Benedetto, 500ml.",
                "Bevanda",
                0.70f,
                toBitmap(R.drawable.naturale))
        );

        products.put("Acqua Frizzante", new Product(
                "Acqua Frizzante",
                "Acqua frizzante San Benedetto, 500ml.",
                "Bevanda",
                0.70f,
                toBitmap(R.drawable.frizzante))
        );

        products.put("Succo alla Pera", new Product(
                "Succo alla Pera",
                "Brik da 200ml di succo alla pera Yoga Optimum.\n\n" +
                        "Valori medi per 100ml:\n" +
                        "55kcal, 0,1g grassi, 13,2g carboidrati, 0,2g proteine",
                "Bevanda",
                0.50f,
                toBitmap(R.drawable.pearjuice))
        );

        products.put("Coca Cola", new Product(
                "Coca Cola",
                "Lasciati cullare dal sapore e dallo stupore che soltanto l'originale ricetta americana può donarti.\n" +
                        "Lattina da 355ml.\n\n" +
                        "Valori medi per 100ml:\n" +
                        "42kcal, 10,6g zuccheri",
                "Bevanda",
                0.90f,
                toBitmap(R.drawable.cocacola))
        );

        products.put("Thè alla Pesca", new Product(
                "Thè alla Pesca",
                "Dalle piantagioni dello Sri Lanka arrivano le migliori foglie per un thé naturalmente buono.\n" +
                        "Bottiglietta da 400ml.\n\n" +
                        "Valori medi per 100ml:\n" +
                        "36kcal, 8,9g zuccheri",
                "Bevanda",
                0.90f,
                toBitmap(R.drawable.pesca))
        );

        cart = new Cart(CART_SIZE);
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

    /* public void newCart(ArrayList<Product> products) {
        cart = new Cart(CART_SIZE);

        products.forEach(p -> cart.addProduct(p));
    } */

    public void addToCart(Product product) {
        getCart().getItems().add(new Cart.CartItem(product, 1));
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
