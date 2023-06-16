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
import android.widget.PopupMenu;

import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.databinding.ActivityHomeBinding;
import com.monopalla.automat.ui.admin.AdminLoginFragment;
import com.monopalla.automat.ui.admin.home.AdminHomeActivity;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.R;
import com.monopalla.automat.ui.user.RegisterFragment;
import com.monopalla.automat.ui.user.UserProfileActivity;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.utils.ImageUtils;
import com.monopalla.automat.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        binding.homeAppBar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.open();
        });

        //
        // Registration banner
        //
        binding.bannerRegisterButton.setOnClickListener(v -> {
            RegisterFragment fragment = new RegisterFragment();
            fragment.show(getSupportFragmentManager(), "register");
        });

        binding.closeBannerButton.setOnClickListener(v -> {
            binding.registerInviteBanner.setVisibility(View.GONE);
        });

        //
        // Navigation drawer settings
        //
        Menu sideMenu = binding.naigationView.getMenu();

        sideMenu.findItem(R.id.login).setVisible(true);

        if (userData.isCurrentUserValid()) {
            sideMenu.findItem(R.id.login).setVisible(false);

            if (userData.isAdmin(user)) {
                sideMenu.findItem(R.id.adminSection).setVisible(true);
            }
            else {
                sideMenu.findItem(R.id.adminSection).setVisible(false);
            }

            sideMenu.findItem(R.id.userProfile).setVisible(true);
            sideMenu.findItem(R.id.logout).setVisible(true);
        }

        EventBus.getDefault().register(this);

        binding.naigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.login:
                    LoginFragment loginFragment = new LoginFragment();
                    loginFragment.show(getSupportFragmentManager(), "login");
                    break;

                case R.id.logout:
                    UIUtils.showNoActionSnackbar(binding.naigationView);

                    break;

                case R.id.userProfile:
                    Intent intent = new Intent(this, UserProfileActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

                    binding.drawerLayout.close();
                    break;

                case R.id.adminAreaLink:
                    Intent adminIntent = new Intent(this, AdminHomeActivity.class);
                    startActivity(adminIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

                    binding.drawerLayout.close();
                    break;
            }

            return true;
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
        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        Menu menu = binding.naigationView.getMenu();

        if (userData.isCurrentUserValid()) {
            binding.registerInviteBanner.setVisibility(View.GONE);

            menu.findItem(R.id.login).setVisible(false);

            if (userData.isAdmin(user)) {
                menu.findItem(R.id.adminSection).setVisible(true);
            }
            else {
                menu.findItem(R.id.adminSection).setVisible(false);
            }

            menu.findItem(R.id.userProfile).setVisible(true);
            menu.findItem(R.id.logout).setVisible(true);
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

    @Subscribe
    public void onSuccessfulLogout(UserRepository.LogoutEvent logoutEvent) {
        binding.registerInviteBanner.setVisibility(View.VISIBLE);

        Menu menu = binding.naigationView.getMenu();

        menu.findItem(R.id.login).setVisible(true);
        menu.findItem(R.id.userProfile).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.adminSection).setVisible(false);
    }
}