package com.monopalla.automat.ui.admin.machine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityMachineControlPanelBinding;
import com.monopalla.automat.utils.UIUtils;

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

        binding.machineAppBar.setNavigationOnClickListener(v -> {
            finishAfterTransition();
        });

        binding.machineName.setText(machine.getName());

        binding.inventoryAction.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryActivity.class);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);

            this.startActivity(intent, options.toBundle());
        });

        binding.balanceAction.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.panelActions, binding.quickActions
        ));

        binding.transactionsAction.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.panelActions, binding.quickActions
        ));

        binding.repairReportAction.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.panelActions, binding.quickActions
        ));

        binding.bugReportAction.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.panelActions, binding.quickActions
        ));

        binding.unlock.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.quickActions, binding.quickActions
        ));

        binding.power.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.quickActions, binding.quickActions
        ));
    }
}