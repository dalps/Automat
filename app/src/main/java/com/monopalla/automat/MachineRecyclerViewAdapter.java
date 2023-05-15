package com.monopalla.automat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;

import java.util.ArrayList;

public class MachineRecyclerViewAdapter extends RecyclerView.Adapter<MachineRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Machine> machineList;

    public MachineRecyclerViewAdapter(ArrayList<Machine> machineList) {
        this.machineList = machineList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView machineNameTV;
        private final TextView machineStatusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            machineNameTV = itemView.findViewById(R.id.machineName);
            machineStatusTV = itemView.findViewById(R.id.machineStatus);
        }


        public TextView getMachineNameTV() {
            return machineNameTV;
        }
        public TextView getMachineStatusTV() {
            return machineStatusTV;
        }
    }

    @NonNull
    @Override
    public MachineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineRecyclerViewAdapter.ViewHolder holder, int position) {
        Machine machine = machineList.get(position);
        Resources res = holder.itemView.getResources();

        TextView machineNameTV = holder.getMachineNameTV();
        TextView machineStatusTV = holder.getMachineStatusTV();

        Drawable statusIcon;

        if(machine.getStatus().contains("Pronto")) {
            statusIcon = res.getDrawable(R.drawable.ic_baseline_check_24, null);
        }
        else {
            statusIcon = res.getDrawable(R.drawable.ic_baseline_warning_24, null);
        }
        
        machineNameTV.setText(res.getString(R.string.rv_machine_name, machine.getName()));
        machineStatusTV.setText(Utils.decorateText(res.getString(R.string.rv_machine_status, machine.getStatus()), statusIcon));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bruh");
            }
        });
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

}
