package com.monopalla.automat.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.databinding.FragmentMainBinding;

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
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.machineSection, binding.machineSection);
            }
            else {
                AnimUtils.switchViewsWithCircularRevealAndDelay(binding.noMachineFound, binding.machineSection);
            }
        });

        return binding.getRoot();
    }
}