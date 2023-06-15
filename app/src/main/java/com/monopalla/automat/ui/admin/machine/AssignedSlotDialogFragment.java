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
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.MachineSlot;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.SlotAssignedDialogBinding;
import com.monopalla.automat.ui.payment.OrderDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class AssignedSlotDialogFragment extends DialogFragment {
    SlotAssignedDialogBinding binding;
    MachineSlot slot;

    public AssignedSlotDialogFragment(MachineSlot slot) {
        this.slot = slot;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding = SlotAssignedDialogBinding.inflate(getLayoutInflater());

        Product product = slot.getProduct();

        binding.productPageTitle.setText(getString(R.string.details_title, product.getName()));

        binding.productPagePic.setImageBitmap(product.getPicture());
        binding.assignedTo.setText(getString(R.string.slot_assigned_to, product.getName()));
        binding.assignedToUnits.setText(getString(R.string.slot_assigned_to_units, slot.getNumberOfItems()));

        binding.productPageTitle.setText(slot.getName());
        binding.productPageAppBar.setNavigationOnClickListener(v -> {
            dismiss();
        });

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
        return inflater.inflate(R.layout.slot_assigned_dialog, container, false);
    }
}