package com.monopalla.automat.ui.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityOrderCompleteBinding;
import com.monopalla.automat.ui.ProductThumbnailRecyclerViewAdapter;
import com.monopalla.automat.ui.home.HomeActivity;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.ui.user.RegisterFragment;

public class OrderCompleteActivity extends AppCompatActivity  implements LoginFragment.LoginListener, RegisterFragment.RegisterListener {
    ActivityOrderCompleteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserRepository userData = UserRepository.getInstance(getApplicationContext());
        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());

        Order order = ProductRepository.getInstance(getApplicationContext()).getCart().toOrder();

        ProductThumbnailRecyclerViewAdapter adapter = new ProductThumbnailRecyclerViewAdapter(order.getItems());

        binding.orderCompleteItemThumbnails.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.orderCompleteItemThumbnails.setAdapter(adapter);

        binding.plusAutomatPoints.setText(getString(R.string.plus_automat_points, order.earnedAutomats()));
        binding.earnedAutomatsMessage.setText(getString(R.string.earned_automat_points_message, order.earnedAutomats()));

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
                LoginFragment fragment = new LoginFragment();
                fragment.show(getSupportFragmentManager(), "login");
            });
        }

        binding.orderCompleteAppbarSubtitle.setText(machineData.getCurrentMachine().getName());

        binding.orderCompleteAppBar.setNavigationOnClickListener(view -> {
            finishAfterTransition();
        });

        binding.backToHomeButton.setOnClickListener(v -> {
            ProductRepository.getInstance(getApplicationContext()).clearCart();
            machineData.setCurrentMachine(null);

            Intent intent = new Intent(this, HomeActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });

        binding.keepShoppingButton.setOnClickListener(v -> {
            finishAfterTransition();
        });
    }

    @Override
    public void onSuccessfulLogin(LoginFragment login) {
        User user = UserRepository.getInstance(getApplicationContext()).getCurrentUser();
        user.addAutomats((int) ProductRepository.getInstance(getApplicationContext()).getCart().toOrder().earnedAutomats());

        binding.newAutomatsBalanceMessage.setVisibility(View.VISIBLE);
        binding.loginToRedeemLink.setVisibility(View.GONE);

        binding.newAutomatsBalanceMessage.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
    }

    @Override
    public void onSuccessfulRegister(LoginFragment login) {
        User user = UserRepository.getInstance(getApplicationContext()).getCurrentUser();
        user.addAutomats((int) ProductRepository.getInstance(getApplicationContext()).getCart().toOrder().earnedAutomats());

        binding.newAutomatsBalanceMessage.setVisibility(View.VISIBLE);
        binding.loginToRedeemLink.setVisibility(View.GONE);

        binding.newAutomatsBalanceMessage.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
    }
}