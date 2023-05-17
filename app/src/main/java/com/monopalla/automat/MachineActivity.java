package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.data.model.Product;

import java.util.ArrayList;

public class MachineActivity extends AppCompatActivity {
    private Machine machine;
    private MachineRepository machineData;
    private ArrayList<Product> products;

    private RecyclerView productsRV;
    private ProductRecyclerViewAdapter adapter;
    private MaterialToolbar appBar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        machineData = MachineRepository.getInstance(getApplicationContext());
        machine = machineData.getCurrentMachine();
        adapter = new ProductRecyclerViewAdapter(machine.getProducts());

        appBar = findViewById(R.id.machineAppBar);
        productsRV = findViewById(R.id.productList);
        subtitle = findViewById(R.id.appbarSubtitle);
        productsRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        productsRV.setAdapter(adapter);

        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });

        subtitle.setText(getString(R.string.machine_activity_subtitle, machine.getName()));
    }
}