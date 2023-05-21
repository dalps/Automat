package com.monopalla.automat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.ActivityProductBinding;
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

        binding.productPageAppBar.setNavigationOnClickListener(view -> finishAfterTransition());

        binding.addToCartButton.setOnClickListener(view -> {
            productData.addToCart(product);
            finishAfterTransition();
        });

        binding.purchaseButton.setOnClickListener(view -> {
            if(!productData.isCartEmpty()) {
                /* TODO show cart deletion confirmation dialog
                productData.emptyCart();
                productData.addToCart(product); */
            }

            PaymentDialogFragment paymentBottomSheet = new PaymentDialogFragment(
                    new ArrayList<>(Collections.singletonList(product)));
            paymentBottomSheet.show(getSupportFragmentManager(), "payment");
        });

    }
}