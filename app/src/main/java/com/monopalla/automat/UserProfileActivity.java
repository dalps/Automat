package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityUserProfileBinding;

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
            binding.orderHistorySize.setText(getString(R.string.user_order_tally, user.historyLength()));

            Bitmap photo = user.getProfilePicture();
            if(photo != null) {
                binding.userProfilePic.setImageBitmap(ImageUtils.roundCrop(photo));
            }
        }
    }
}