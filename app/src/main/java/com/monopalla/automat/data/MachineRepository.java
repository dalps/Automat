package com.monopalla.automat.data;

import android.content.Context;

import com.monopalla.automat.data.model.Machine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MachineRepository {
    private static MachineRepository instance;
    private final Map<String, Machine> machines = new HashMap<>();
    private Machine currentMachine;

    private MachineRepository(Context context) {
        ProductRepository products = ProductRepository.getInstance(context);

        Machine temp = new Machine("ATM-00011034",
                "Automat del Palazzo #1",
                "Cagliari",
                "Pronto");
        temp.assignSlot("A01", products.getProduct("Fiesta"), 18);
        temp.assignSlot("A02", products.getProduct("Oreo"), 20);
        temp.assignSlot("A03", products.getProduct("Tuc"), 25);
        temp.assignSlot("A04", products.getProduct("Mars"), 25);
        temp.assignSlot("A05", products.getProduct("Crackers"), 30);
        temp.assignSlot("A06", products.getProduct("Coca Cola"), 22);
        temp.assignSlot("A07", products.getProduct("The alla Pesca"), 14);
        machines.put(temp.getSerialNumber(), temp);

        temp = new Machine("ATM-00011035",
                "Automat del Palazzo #2",
                "Cagliari",
                "Occupato");
        temp.assignSlot("A01", products.getProduct("Fiesta"), 33);
        temp.assignSlot("A02", products.getProduct("Oreo"), 28);
        temp.assignSlot("A03", products.getProduct("Fonzies"), 27);
        temp.assignSlot("A04", products.getProduct("Tuc"), 12);
        temp.assignSlot("A05", products.getProduct("Acqua Naturale"), 21);
        temp.assignSlot("A06", products.getProduct("Acqua Frizzante"), 20);
        temp.assignSlot("A07", products.getProduct("Succo alla Pera"), 10);
        machines.put(temp.getSerialNumber(), temp);
    }

    public static MachineRepository getInstance(Context context) {
        if(instance == null) {
            instance = new MachineRepository(context);
        }

        return instance;
    }

    public static MachineRepository getInstance() {
        return instance;
    }

    public Machine getMachine(String serialNumber) {
        return machines.get(serialNumber);
    }

    public ArrayList<Machine> getMachines() {
        return new ArrayList<>(machines.values());
    }

    public Machine getCurrentMachine() {
        return currentMachine;
    }

    public void setCurrentMachine(Machine currentMachine) {
        this.currentMachine = currentMachine;
    }
}
