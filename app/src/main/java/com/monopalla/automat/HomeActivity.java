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

        binding.scanFAB.setOnClickListener(view ->
                AnimUtils.switchViewsWithCircularReveal(binding.noMachineFound, binding.machineSection));
    }
}