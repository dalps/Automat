package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        System.out.println(machineData.getMachines().size());

        binding.machineList.setLayoutManager(new LinearLayoutManager(this));
        binding.machineList.setAdapter(new MachineRecyclerViewAdapter(machineData.getMachines()));

        binding.bottomNav.setSelectedItemId(R.id.home);

        binding.scanHelp.setText(Utils.decorateText(getString(R.string.home_scan_help),
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_refresh_24, null)));

        binding.scanFAB.setOnClickListener(view -> {
            binding.noMachineFound.setVisibility(View.GONE);
            binding.detectedMachines.setVisibility(View.VISIBLE);
        });
    }
}