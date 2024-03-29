package com.monopalla.automat.ui.admin.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityAdminHomeBinding;
import com.monopalla.automat.databinding.AdminMachineItemBinding;
import com.monopalla.automat.ui.admin.machine.MachineControlPanelActivity;
import com.monopalla.automat.ui.machine.MachineActivity;
import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.utils.UIUtils;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {
    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());

        binding.machineList.setLayoutManager(new LinearLayoutManager(this));
        binding.machineList.setAdapter(new AdminMachineRecyclerViewAdapter(machineData.getMachines()));

        binding.scanFAB.setOnClickListener(view -> {
            if (binding.machineSection.getVisibility() == View.VISIBLE) {
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.machineSection, binding.machineSection, binding.spinwheel);
            }
            else {
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.noMachineFound, binding.machineSection, binding.spinwheel);
            }
        });

        binding.detectedCount.setText(getString(R.string.detected_count, machineData.getMachines().size()));

        binding.homeAppBar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.open();
        });

        binding.naigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.logout) {
                finishAfterTransition();
                return true;
            }

            UIUtils.showNoActionSnackbar(binding.naigationView);
            return false;
        });
    }

    class AdminMachineRecyclerViewAdapter extends RecyclerView.Adapter<AdminMachineRecyclerViewAdapter.ViewHolder> {
        private ArrayList<Machine> machineList;

        public AdminMachineRecyclerViewAdapter(ArrayList<Machine> machineList) {
            this.machineList = machineList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView machineNameTV;
            final TextView machineSerialNumber;
            final TextView machineStatusTV;
            final LinearLayout controlPanelLink;

            final MaterialButton unlockBtn;

            final MaterialButton powerBtn;

            public ViewHolder(AdminMachineItemBinding binding) {
                super(binding.getRoot());

                machineNameTV = binding.machineName;
                machineSerialNumber = binding.machineSerialNumber;
                machineStatusTV = binding.machineStatus;
                controlPanelLink = binding.machineInfo;
                unlockBtn = binding.unlock;
                powerBtn = binding.power;
            }
        }

        @NonNull
        @Override
        public AdminMachineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(AdminMachineItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdminMachineRecyclerViewAdapter.ViewHolder holder, int position) {
            Machine machine = machineList.get(position);
            Context context = holder.itemView.getContext();
            MachineRepository machineData = MachineRepository.getInstance(context);

            holder.machineNameTV.setText(context.getString(R.string.rv_machine_name, machine.getName()));
            holder.machineStatusTV.setText(context.getString(R.string.rv_machine_status, machine.getStatus()));
            holder.machineSerialNumber.setText(machine.getSerialNumber());

            holder.controlPanelLink.setOnClickListener(view -> {
                machineData.setCurrentMachine(machine);

                Intent intent = new Intent(context, MachineControlPanelActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context);

                context.startActivity(intent, options.toBundle());
            });

            holder.unlockBtn.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                    holder.unlockBtn, binding.scanFAB
            ));

            holder.powerBtn.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                    holder.powerBtn, binding.scanFAB
            ));
        }

        @Override
        public int getItemCount() {
            return machineList.size();
        }
    }
}