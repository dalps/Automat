package com.monopalla.automat.ui.user;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityHomeBinding;
import com.monopalla.automat.databinding.FragmentRegisterBinding;

public class RegisterFragment extends DialogFragment {
    FragmentRegisterBinding binding;

    public interface RegisterListener {
        public void onSuccessfulRegister(LoginFragment login);
    }

    RegisterListener listener;

    public RegisterFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        UserRepository userData = UserRepository.getInstance(getContext());

        binding = FragmentRegisterBinding.inflate(getLayoutInflater());

        binding.registerButton.setOnClickListener(v -> {
            String usernameInput = binding.registerUsernameEditText.getText().toString();
            String nameInput = binding.registerNameEditText.getText().toString() +
                    binding.registerLastNameEditText.getText().toString();
            String passwordInput = binding.registerPasswordEditText.getText().toString();

            // TODO validate inputs

            User newUser = new User(usernameInput, passwordInput, nameInput, null);
            userData.register(newUser);
            userData.setCurrentUser(newUser);

            Intent intent = new Intent(getActivity(), UserProfileActivity.class);

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            dismiss();
        });

        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RegisterListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement RegisterListener");
        }
    }
}