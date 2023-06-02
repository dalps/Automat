package com.monopalla.automat.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.monopalla.automat.databinding.ActivityHomeBinding;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.R;
import com.monopalla.automat.ui.user.RegisterFragment;
import com.monopalla.automat.ui.user.UserProfileActivity;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.utils.ImageUtils;

public class HomeActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.RegisterListener {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        // Bottom navigation interaction
        //
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.favorites:
                    selectedFragment = new FavoritesFragment();
                    break;


                case R.id.home:
                    selectedFragment = new MainFragment();
                    break;


                case R.id.orderHistory:
                    selectedFragment = new HistoryFragment();
                    break;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();

            return true;
        });

        binding.bottomNav.setSelectedItemId(R.id.home);

        // Display the first fragment at application start
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new MainFragment())
                .commit();


        //
        // App bar user avatar interaction & appearance
        //
        Menu menu = binding.homeAppBar.getMenu();
        MenuItem loginButton = menu.findItem(R.id.login);

        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        if (userData.isCurrentUserValid()) {
            Bitmap cropped = ImageUtils.roundCrop(user.getProfilePicture());
            loginButton.setIcon(new BitmapDrawable(getResources(), cropped));
        }

        binding.homeAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.login) {
                if (userData.isCurrentUserValid()) {
                    Intent intent = new Intent(this, UserProfileActivity.class);

                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    LoginFragment fragment = new LoginFragment();
                    fragment.show(getSupportFragmentManager(), "login");
                }

                return true;
            }

            return false;
        });

        //
        // Registration banner
        //
        binding.bannerRegisterButton.setOnClickListener(v -> {
            RegisterFragment fragment = new RegisterFragment();
            fragment.show(getSupportFragmentManager(), "register");
        });

        binding.bannerLoginButton.setOnClickListener(v -> {
            LoginFragment fragment = new LoginFragment();
            fragment.show(getSupportFragmentManager(), "login");
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (UserRepository.getInstance(getApplicationContext()).isCurrentUserValid()) {
                binding.registerInviteBanner.setVisibility(View.GONE);
            }
            else {
                binding.registerInviteBanner.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSuccessfulLogin(LoginFragment login) {
        Menu menu = binding.homeAppBar.getMenu();
        MenuItem loginButton = menu.findItem(R.id.login);

        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        if (userData.isCurrentUserValid()) {
            binding.registerInviteBanner.setVisibility(View.GONE);

            Bitmap cropped = ImageUtils.roundCrop(user.getProfilePicture());
            loginButton.setIcon(new BitmapDrawable(getResources(), cropped));
        }

        Intent intent = new Intent(this, UserProfileActivity.class);

        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onSuccessfulRegister(LoginFragment login) {
        if (UserRepository.getInstance(getApplicationContext()).isCurrentUserValid()) {
            binding.registerInviteBanner.setVisibility(View.GONE);
        }
    }
}