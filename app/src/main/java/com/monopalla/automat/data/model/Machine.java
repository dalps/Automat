package com.monopalla.automat.data.model;

import java.util.HashMap;
import java.util.Objects;

public class Machine {
    private String serialNumber;
    private String name;
    private String position;
    private String status;
    private HashMap<String, MachineSlot> slots;
    private final static int SLOT_CAPACITY = 40;

    public Machine(String serialNumber, String name, String position, String status) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.position = position;
        this.status = status;
        this.slots = new HashMap<>();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, MachineSlot> getSlots() {
        return slots;
    }

    public void setSlots(HashMap<String, MachineSlot> slots) {
        this.slots = slots;
    }

    public void buy(String slotName) {
        MachineSlot slot = slots.get(slotName);

        if(slot == null) {
            return;
        }

        slot.dispenseItem();
    }

    public void refillSlot(String slotName) {
        MachineSlot slot = slots.get(slotName);

        if(slot == null) {
            return;
        }

        slot.refill();
    }

    /**
     * Assign a product to an existing slot or create a new slot to hold it
     **/
    public void assignSlot(String slotName, Product product, int amount) {
        MachineSlot slot = slots.get(slotName);

        if(slot != null) {
            slot.setProduct(product);
            slot.setNumberOfItems(amount);
            return;
        }

        if(slots.size() < SLOT_CAPACITY) {
            System.out.println("Machine " + getSerialNumber() + " has no free slots available");
            return;
        }

        slots.put(slotName, new MachineSlot(slotName, product, amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Machine)) return false;
        Machine machine = (Machine) o;
        return getSerialNumber() == machine.getSerialNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSerialNumber());
    }
}
