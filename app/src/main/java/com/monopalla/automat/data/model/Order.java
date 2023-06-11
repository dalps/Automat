package com.monopalla.automat.data.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    LocalDate date;
    Machine machine;
    ArrayList<Product> items;
    String paymentMethod = OTHER_METHOD;
    public static final double AUTOMATS_CHANGE = 0.1;
    public static final long AUTOMATS_BONUS = 3;
    public static final String POINTS_METHOD = "automats";
    public static final String OTHER_METHOD = "other";

    public Order(LocalDate date, Machine machine, ArrayList<Product> items) {
        this.date = date;
        this.machine = machine;
        this.items = items;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }


    public double total() {
        return items.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public long totalInAutomats() {
        return Math.round(total() / AUTOMATS_CHANGE);
    }

    public long earnedAutomats() {
        return totalInAutomats() + AUTOMATS_BONUS;
    }

    public void removeItem(Product product) {
        items.remove(product);
    }

    public void addItem(Product product) {
        items.add(product);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
