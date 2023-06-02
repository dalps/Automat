package com.monopalla.automat.ui.payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentCompleteOrderDialogBinding;
import com.monopalla.automat.ui.ProductThumbnailRecyclerViewAdapter;
import com.monopalla.automat.ui.user.LoginFragment;

public class CompleteOrderDialogFragment extends DialogFragment {
    FragmentCompleteOrderDialogBinding binding;
    Order order;

    public CompleteOrderDialogFragment(Order order) {
        this.order = order;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding = FragmentCompleteOrderDialogBinding.inflate(getLayoutInflater());

        ProductThumbnailRecyclerViewAdapter adapter = new ProductThumbnailRecyclerViewAdapter(order.getItems());

        binding.orderCompleteItemThumbnails.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.orderCompleteItemThumbnails.setAdapter(adapter);

        binding.earnedAutomatsMessage.setText(getString(R.string.earned_automat_points_message, order.earnedAutomats()));

        UserRepository userData = UserRepository.getInstance(getContext());

        if (userData.isCurrentUserValid()) {
            User user = userData.getCurrentUser();

            binding.newAutomatsBalanceMessage.setVisibility(View.VISIBLE);
            binding.loginToRedeemLink.setVisibility(View.GONE);

            binding.newAutomatsBalanceMessage.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
        }
        else {
            binding.newAutomatsBalanceMessage.setVisibility(View.GONE);
            binding.loginToRedeemLink.setVisibility(View.VISIBLE);

            binding.loginToRedeemLink.setOnClickListener(v -> {
                LoginFragment fragment = new LoginFragment(null);
                fragment.show(getParentFragmentManager(), "login");
            });
        }

        binding.backToHomeButton.setOnClickListener(v -> {
            getActivity().finish();
        });

        binding.keepShoppingButton.setOnClickListener(v -> {
            dismiss();
        });

        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return inflater.inflate(R.layout.fragment_complete_order_dialog, container, false);
    }
}