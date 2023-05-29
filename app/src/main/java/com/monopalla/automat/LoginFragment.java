package com.monopalla.automat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityHomeBinding;
import com.monopalla.automat.databinding.FragmentLoginBinding;

public class LoginFragment extends DialogFragment {
    ActivityHomeBinding parentBiding;

    public LoginFragment(ActivityHomeBinding parentBiding) {
        this.parentBiding = parentBiding;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        UserRepository userData = UserRepository.getInstance(getContext());

        FragmentLoginBinding binding = FragmentLoginBinding.inflate(getLayoutInflater());

        binding.loginButton.setOnClickListener(v -> {
            String usernameInput = binding.loginUsernameEditText.getText().toString();
            String passwordInput = binding.loginPasswordEditText.getText().toString();

            if (userData.login(usernameInput, passwordInput)) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                dismiss();
            }
        });

        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        Menu menu = parentBiding.homeAppBar.getMenu();
        MenuItem loginButton = menu.findItem(R.id.login);

        UserRepository userData = UserRepository.getInstance(getContext());
        User user = userData.getCurrentUser();

        if (user != null) {
            Bitmap cropped = ImageUtils.roundCrop(user.getProfilePicture());
            loginButton.setIcon(new BitmapDrawable(getResources(), cropped));
        }
    }
}