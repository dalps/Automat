package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityMachineBinding;

public class MachineActivity extends AppCompatActivity {
    ActivityMachineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMachineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        Machine machine = machineData.getCurrentMachine();
        ProductRepository productData = ProductRepository.getInstance(getApplicationContext());

        binding.productList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.productList.setAdapter(new ProductRecyclerViewAdapter(machine.getProducts(), binding.checkoutFAB));

        binding.machineAppBar.setNavigationOnClickListener(view -> {
            // TODO show clear cart confirmation dialog (if not empty)
            productData.clearCart();

            finishAfterTransition();
        });

        binding.appbarSubtitle.setText(getString(R.string.machine_activity_subtitle, machine.getName()));

        binding.checkoutFAB.setOnClickListener(view -> {
            if(productData.isCartEmpty()) {
                Snackbar.make(binding.checkoutFAB, getString(R.string.cart_empty_alert), Snackbar.LENGTH_SHORT)
                        .setAnchorView(binding.checkoutFAB)
                        .show();
                return;
            }

            PaymentDialogFragment paymentBottomSheet = new PaymentDialogFragment(
                    productData.getCart());
            paymentBottomSheet.show(getSupportFragmentManager(), "payment");
        });
    }
}