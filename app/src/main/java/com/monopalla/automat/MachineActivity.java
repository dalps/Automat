package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityMachineBinding;

public class MachineActivity extends AppCompatActivity {
    ActivityMachineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMachineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        Machine machine = machineData.getCurrentMachine();

        binding.productList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.productList.setAdapter(new ProductRecyclerViewAdapter(machine.getProducts()));

        binding.machineAppBar.setNavigationOnClickListener(view -> finishAfterTransition());
        binding.appbarSubtitle.setText(getString(R.string.machine_activity_subtitle, machine.getName()));
    }
}