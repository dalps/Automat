package com.monopalla.automat.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.monopalla.automat.databinding.ActivityHomeBinding;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.R;
import com.monopalla.automat.ui.user.UserProfileActivity;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;

public class HomeActivity extends AppCompatActivity {
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
            loginButton.setIcon(new BitmapDrawable(getResources(), user.getProfilePicture()));
        }

        binding.homeAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.login) {
                if (userData.isCurrentUserValid()) {
                    Intent intent = new Intent(this, UserProfileActivity.class);

                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    LoginFragment fragment = new LoginFragment(binding);
                    fragment.show(getSupportFragmentManager(), "login");
                }

                return true;
            }

            return false;
        });
    }
}