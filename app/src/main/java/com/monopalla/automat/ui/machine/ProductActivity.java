package com.monopalla.automat.ui.machine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.Cart;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.ActivityProductBinding;
import com.monopalla.automat.ui.payment.OrderDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProductRepository productData = ProductRepository.getInstance(this);
        Product product = productData.getCurrentProduct();

        binding.productPagePic.setImageBitmap(product.getPicture());
        binding.productPageName.setText(product.getName());
        binding.productPagePrice.setText(getString(R.string.product_price, product.getPrice()));
        binding.productPageTitle.setText(getString(R.string.product_page_title, product.getName()));
        binding.productPageDescription.setText(product.getDescription());

        binding.productPageAppBar.setNavigationOnClickListener(view -> finishAfterTransition());

        binding.productPageAddToCartButton.setOnClickListener(view -> {
            productData.addToCart(product);
            finishAfterTransition();
        });

        binding.purchaseButton.setOnClickListener(view -> {
            OrderDialogFragment fragment = new OrderDialogFragment(
                    new Order(LocalDate.now(),
                            MachineRepository.getInstance().getCurrentMachine(),
                            new ArrayList<>(Collections.singletonList(product))));

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .add(R.id.productPageOrderFragmentContainer, fragment, "order")
                    .addToBackStack("order")
                    .commit();
        });

    }
}