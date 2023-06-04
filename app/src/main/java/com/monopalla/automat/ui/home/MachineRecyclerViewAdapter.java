package com.monopalla.automat.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.MachineRecyclerviewItemBinding;
import com.monopalla.automat.ui.machine.MachineActivity;

import java.util.ArrayList;

public class MachineRecyclerViewAdapter extends RecyclerView.Adapter<MachineRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Machine> machineList;

    public MachineRecyclerViewAdapter(ArrayList<Machine> machineList) {
        this.machineList = machineList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView machineNameTV;
        final TextView machineStatusTV;

        public ViewHolder(MachineRecyclerviewItemBinding binding) {
            super(binding.getRoot());

            machineNameTV = binding.machineName;
            machineStatusTV = binding.machineStatus;
        }
    }

    @NonNull
    @Override
    public MachineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(MachineRecyclerviewItemBinding.inflate(
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
