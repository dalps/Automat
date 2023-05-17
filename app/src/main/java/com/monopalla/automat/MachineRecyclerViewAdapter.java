package com.monopalla.automat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.model.Machine;

import java.util.ArrayList;

public class MachineRecyclerViewAdapter extends RecyclerView.Adapter<MachineRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Machine> machineList;
    private MachineRepository machineData = MachineRepository.getInstance();

    public MachineRecyclerViewAdapter(ArrayList<Machine> machineList) {
        this.machineList = machineList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView machineNameTV;
        private final TextView machineStatusTV;
        private final RelativeLayout machineLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            machineNameTV = itemView.findViewById(R.id.machineName);
            machineStatusTV = itemView.findViewById(R.id.machineStatus);
            machineLayout = itemView.findViewById(R.id.machineContainer);
        }


        public TextView getMachineNameTV() {
            return machineNameTV;
        }
        public TextView getMachineStatusTV() {
            return machineStatusTV;
        }
        public RelativeLayout getMachineLayout() {
            return machineLayout;
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
        Context context = holder.itemView.getContext();

        TextView machineNameTV = holder.getMachineNameTV();
        TextView machineStatusTV = holder.getMachineStatusTV();
        RelativeLayout machineLayout = holder.getMachineLayout();
        
        machineNameTV.setText(res.getString(R.string.rv_machine_name, machine.getName()));
        machineStatusTV.setText(res.getString(R.string.rv_machine_status, machine.getStatus()));

        machineLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, MachineActivity.class);

            // intent.putExtra("MACHINE", machine);
            machineData.setCurrentMachine(machine);
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

}
