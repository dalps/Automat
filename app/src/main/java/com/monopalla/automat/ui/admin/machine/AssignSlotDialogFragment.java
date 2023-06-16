package com.monopalla.automat.ui.admin.machine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.MachineSlot;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.SlotAssignDialogBinding;
import com.monopalla.automat.utils.UIUtils;

public class AssignSlotDialogFragment extends DialogFragment {
    SlotAssignDialogBinding binding;
    MachineSlot slot;

    public AssignSlotDialogFragment(MachineSlot slot) {
        this.slot = slot;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding = SlotAssignDialogBinding.inflate(getLayoutInflater());

        binding.productPageTitle.setText(getString(R.string.slot_assign_title, slot.getName()));
        
        binding.productPageAppBar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        binding.barcodeMethod.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.barcodeMethod, binding.barcodeMethod
        ));

        binding.catalogueMethod.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.catalogueMethod, binding.barcodeMethod
        ));

        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.slot_assign_dialog, container, false);
    }
}