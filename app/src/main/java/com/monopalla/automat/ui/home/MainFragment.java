package com.monopalla.automat.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.MachineItemBinding;
import com.monopalla.automat.ui.machine.MachineActivity;
import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.databinding.FragmentMainBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    FragmentMainBinding binding;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        MachineRepository machineData = MachineRepository.getInstance(getContext());

        binding.machineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.machineList.setAdapter(new MachineRecyclerViewAdapter(machineData.getMachines()));

        binding.scanFAB.setOnClickListener(view -> {
            if (binding.machineSection.getVisibility() == View.VISIBLE) {
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.machineSection, binding.machineSection, binding.spinwheel);
            }
            else {
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.noMachineFound, binding.machineSection, binding.spinwheel);
            }
        });

        return binding.getRoot();
    }
}

class MachineRecyclerViewAdapter extends RecyclerView.Adapter<MachineRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Machine> machineList;

    public MachineRecyclerViewAdapter(ArrayList<Machine> machineList) {
        this.machineList = machineList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView machineNameTV;
        final TextView machineStatusTV;

        public ViewHolder(MachineItemBinding binding) {
            super(binding.getRoot());

            machineNameTV = binding.machineName;
            machineStatusTV = binding.machineStatus;
        }
    }

    @NonNull
    @Override
    public MachineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(MachineItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineRecyclerViewAdapter.ViewHolder holder, int position) {
        Machine machine = machineList.get(position);
        Context context = holder.itemView.getContext();
        MachineRepository machineData = MachineRepository.getInstance(context);

        holder.machineNameTV.setText(context.getString(R.string.rv_machine_name, machine.getName()));
        holder.machineStatusTV.setText(context.getString(R.string.rv_machine_status, machine.getStatus()));

        holder.itemView.setOnClickListener(view -> {
            machineData.setCurrentMachine(machine);

            Intent intent = new Intent(context, MachineActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context);

            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }
}