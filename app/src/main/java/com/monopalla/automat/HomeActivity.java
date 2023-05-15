package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    RecyclerView machinesRV;
    MachineRecyclerViewAdapter adapter;
    MachineRepository machineData;
    FloatingActionButton scanFAB;
    View noMachineFoundView;
    View machineListView;
    TextView scanHelpTV;
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        machineData = MachineRepository.getInstance(getApplicationContext());

        machinesRV = findViewById(R.id.machineList);
        machinesRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MachineRecyclerViewAdapter((ArrayList<Machine>) machineData.getMachines());
        machinesRV.setAdapter(adapter);
        scanFAB = findViewById(R.id.scanFAB);
        noMachineFoundView = findViewById(R.id.noMachineFound);
        machineListView = findViewById(R.id.detectedMachines);
        scanHelpTV = findViewById(R.id.scanHelp);
        bottomNavView = findViewById(R.id.bottomNav);

        bottomNavView.setSelectedItemId(R.id.home);

        scanHelpTV.setText(Utils.decorateText(getString(R.string.home_scan_help),
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_refresh_24, null)));

        scanFAB.setOnClickListener(view -> {
            noMachineFoundView.setVisibility(View.GONE);
            machineListView.setVisibility(View.VISIBLE);
        });
    }
}