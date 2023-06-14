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
import com.monopalla.automat.ui.machine.MachineActivity;
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
        ProductRepository productData = ProductRepository.getInstance(getApplicationContext());

        User user = userData.getCurrentUser();
        Order order = productData.getCurrentOrder();

        ProductThumbnailRecyclerViewAdapter adapter = new ProductThumbnailRecyclerViewAdapter(order.getItems());

        binding.orderCompleteItemThumbnails.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.orderCompleteItemThumbnails.setAdapter(adapter);

        // binding.plusAutomatPoints.setText(getString(R.string.plus_automat_points, order.earnedAutomats()));
        binding.earnedAutomatsMessage.setText(getString(R.string.earned_automat_points_message, order.earnedAutomats()));

        // Show earned points message user didn't pay with points
        if (order.getPaymentMethod().compareTo(Order.POINTS_METHOD) == 0) {
            binding.earnedAutomatsMessage.setVisibility(View.GONE);
            
            binding.newBalance.setVisibility(View.VISIBLE);
            binding.newBalance.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
        }
        else {
            if (userData.isCurrentUserValid()) {
                user.addAutomats((int) order.earnedAutomats());
            }

            binding.earnedAutomatsMessage.setVisibility(View.VISIBLE);
        }

        if (userData.isCurrentUserValid()) {
            binding.loginToRedeemLink.setVisibility(View.GONE);

            binding.newBalance.setVisibility(View.VISIBLE);
            binding.newBalance.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
        }
        else {
            binding.newBalance.setVisibility(View.GONE);

            binding.loginToRedeemLink.setVisibility(View.VISIBLE);
            binding.loginToRedeemLink.setOnClickListener(v -> {
                LoginFragment fragment = new LoginFragment();
                fragment.show(getSupportFragmentManager(), "login");
            });
        }

        binding.orderCompleteAppbarSubtitle.setText(machineData.getCurrentMachine().getName());

        binding.orderCompleteAppBar.setNavigationOnClickListener(view -> {
            productData.clearCart();

            // Back to the machine's product listing
            Intent intent = new Intent(this, MachineActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });

        binding.backToHomeButton.setOnClickListener(v -> {
            productData.clearCart();
            machineData.setCurrentMachine(null);

            // Back to home
            Intent intent = new Intent(this, HomeActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });

        binding.keepShoppingButton.setOnClickListener(v -> {
            productData.clearCart();

            // Back to the machine's product listing
            Intent intent = new Intent(this, MachineActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });
    }

    @Override
    public void onSuccessfulLogin(LoginFragment login) {
        User user = UserRepository.getInstance(getApplicationContext()).getCurrentUser();
        user.addAutomats((int) ProductRepository.getInstance(getApplicationContext()).getCart().toOrder().earnedAutomats());

        binding.newBalance.setVisibility(View.VISIBLE);
        binding.loginToRedeemLink.setVisibility(View.GONE);

        binding.newBalance.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
    }

    @Override
    public void onSuccessfulRegister(LoginFragment login) {
        User user = UserRepository.getInstance(getApplicationContext()).getCurrentUser();
        user.addAutomats((int) ProductRepository.getInstance(getApplicationContext()).getCart().toOrder().earnedAutomats());

        binding.newBalance.setVisibility(View.VISIBLE);
        binding.loginToRedeemLink.setVisibility(View.GONE);

        binding.newBalance.setText(getString(R.string.new_automat_points_balance_message, user.getAutomats()));
    }
}