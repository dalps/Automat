package com.monopalla.automat.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.databinding.FragmentAdminLoginBinding;

public class AdminLoginFragment extends DialogFragment {
    FragmentAdminLoginBinding binding;

    public AdminLoginFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        UserRepository userData = UserRepository.getInstance(getContext());

        binding = FragmentAdminLoginBinding.inflate(getLayoutInflater());

        binding.loginButton.setOnClickListener(v -> {
            String usernameInput = binding.loginUsernameEditText.getText().toString();
            String passwordInput = binding.loginPasswordEditText.getText().toString();

            if (userData.login(usernameInput, passwordInput)) {
                Intent intent = new Intent(getActivity(), AdminHomeActivity.class);

                startActivity(intent);
            }
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

        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}