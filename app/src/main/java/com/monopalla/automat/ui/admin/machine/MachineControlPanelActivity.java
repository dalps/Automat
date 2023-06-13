package com.monopalla.automat.ui.admin.machine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityMachineControlPanelBinding;

public class MachineControlPanelActivity extends AppCompatActivity {
    ActivityMachineControlPanelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMachineControlPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        Machine machine = machineData.getCurrentMachine();

        binding.appbarSubtitle.setText(machine.getSerialNumber());
        binding.machineName.setText(machine.getName());

        binding.inventoryAction.setOnClickListener(v -> {

        });
    }
}