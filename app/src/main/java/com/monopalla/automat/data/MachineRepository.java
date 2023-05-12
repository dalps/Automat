package com.monopalla.automat.data;

import com.monopalla.automat.data.model.Machine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MachineRepository {
    private static MachineRepository instance;
    private final Map<String, Machine> machines = new HashMap<String, Machine>();

    private MachineRepository() {
        ProductRepository products = ProductRepository.getInstance();

        Machine temp = new Machine("ATM-00011034",
                "Automat del Palazzo #1",
                "Cagliari",
                "Disponibile");
        temp.assignSlot("A01", products.getProduct("Fiesta"), 18);
        temp.assignSlot("A02", products.getProduct("Oreo"), 20);
        temp.assignSlot("A03", products.getProduct("Tuc"), 25);
        machines.put(temp.getSerialNumber(), temp);

        temp = new Machine("ATM-00011035",
                "Automat del Palazzo #2",
                "Cagliari",
                "Disponibile");
        temp.assignSlot("A01", products.getProduct("Fiesta"), 33);
        temp.assignSlot("A02", products.getProduct("Oreo"), 28);
        temp.assignSlot("A03", products.getProduct("Fonzies"), 27);
        temp.assignSlot("A04", products.getProduct("Tuc"), 12);
        machines.put(temp.getSerialNumber(), temp);
    }

    public static MachineRepository getInstance() {
        if(instance == null) {
            instance = new MachineRepository();
        }

        return instance;
    }

    public Machine getMachine(String serialNumber) {
        return machines.get(serialNumber);
    }

    public Collection<Machine> getMachines() {
        return machines.values();
    }
}
