package com.monopalla.automat.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityUserProfileBinding;
import com.monopalla.automat.utils.ImageUtils;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        User user = userData.getCurrentUser();

        binding.userProfileAppBar.setNavigationOnClickListener(v -> finishAfterTransition());

        if(user != null) {
            binding.userProfileUsername.setText(user.getName());
            binding.userProfileAutomats.setText(getString(R.string.user_profile_automat_tally, user.getAutomats()));

            Bitmap photo = user.getProfilePicture();
            if(photo != null) {
                binding.userProfilePic.setImageBitmap(ImageUtils.roundCrop(photo));
            }

            binding.logoutButton.setOnClickListener(v -> {
                userData.logout();
                finishAfterTransition();
            });
        }
    }
}