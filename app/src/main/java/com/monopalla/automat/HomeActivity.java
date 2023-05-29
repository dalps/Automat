package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        System.out.println(machineData.getMachines().size());

        binding.machineList.setLayoutManager(new LinearLayoutManager(this));
        binding.machineList.setAdapter(new MachineRecyclerViewAdapter(machineData.getMachines()));

        binding.bottomNav.setSelectedItemId(R.id.home);

        binding.scanFAB.setOnClickListener(view ->
                AnimUtils.switchViewsWithCircularReveal(binding.noMachineFound, binding.machineSection));

        Menu menu = binding.homeAppBar.getMenu();
        MenuItem loginButton = menu.findItem(R.id.login);

        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        if (user != null) {
            loginButton.setIcon(new BitmapDrawable(getResources(), user.getProfilePicture()));
        }

        binding.homeAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.login) {
                if (userData.getCurrentUser() != null) {
                    // TODO Start user profile activity
                    Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);

                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    // TODO Show login dialog
                    LoginFragment fragment = new LoginFragment(binding);
                    fragment.show(getSupportFragmentManager(), "login");
                }

                return true;
            }

            return false;
        });


    }
}