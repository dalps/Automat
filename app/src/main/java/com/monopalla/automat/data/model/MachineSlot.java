package com.monopalla.automat.data.model;

import java.io.Serializable;
import java.util.Objects;

public class MachineSlot implements Comparable<MachineSlot> {
    private String name; // A unique name for the slot within the machine
    private Product product; // The product that is sold
    private int numberOfItems; // The current amount of product items
    public static final int ITEM_CAPACITY = 50; // The maximum number of product items a slot can hold

    public MachineSlot(String name, Product product, int initialAmount) {
        this.name = name;
        this.product = product;
        numberOfItems = initialAmount;
    }

    public MachineSlot(String name) {
        this(name, null, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public boolean isAssigned() {
        boolean flag = product != null;

        if(!flag) {
            System.out.println("Slot " + getName() + " is yet to be assigned");
        }

        return flag;
    }

    public void dispenseItem() {
        if(!isAssigned()) {
            return;
        }

        if(numberOfItems <= 0) {
            System.out.println("Slot " + getName() + " is out of stock");
            return;
        }

        numberOfItems -= 1;
    }

    public void refill() {
        if(!isAssigned()) {
            return;
        }

        numberOfItems = ITEM_CAPACITY;
        System.out.println("Slot " + getName() + " has been refilled");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineSlot)) return false;
        MachineSlot that = (MachineSlot) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(MachineSlot s) {
        return this.getName().compareTo(s.getName());
    }
}
