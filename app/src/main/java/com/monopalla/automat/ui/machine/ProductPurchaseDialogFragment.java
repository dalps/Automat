package com.monopalla.automat.ui.machine;

import static com.monopalla.automat.utils.UIUtils.showSnackbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentProductFavoritesDialogBinding;
import com.monopalla.automat.databinding.FragmentProductPurchaseDialogBinding;
import com.monopalla.automat.ui.payment.OrderDialogFragment;
import com.monopalla.automat.utils.AnimUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ProductPurchaseDialogFragment extends DialogFragment {
    FragmentProductPurchaseDialogBinding binding;
    Product product;

    public ProductPurchaseDialogFragment(Product product) {
        this.product = product;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding = FragmentProductPurchaseDialogBinding.inflate(getLayoutInflater());

        ProductRepository productData = ProductRepository.getInstance(getContext());
        Product product = productData.getCurrentProduct();
        UserRepository userData = UserRepository.getInstance(getContext());
        User user = userData.getCurrentUser();

        binding.productPageTitle.setText(getString(R.string.details_title, product.getName()));

        binding.productPagePic.setImageBitmap(product.getPicture());
        binding.productPageName.setText(product.getName());
        binding.productPagePrice.setText(getString(R.string.product_price, product.getPrice()));
        binding.productPageDescription.setText(product.getDescription());

        binding.productPageAppBar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        if (productData.getCart().getUnits().contains(product)) {
            binding.productPageAddToCartButton.setVisibility(View.INVISIBLE);
            binding.productPageRemoveFromCartButton.setVisibility(View.VISIBLE);
        }
        else {
            binding.productPageAddToCartButton.setVisibility(View.VISIBLE);
            binding.productPageRemoveFromCartButton.setVisibility(View.INVISIBLE);
        }

        binding.productPageAddToCartButton.setOnClickListener(view -> {
            productData.addToCart(product);

            dismiss();
        });

        binding.productPageRemoveFromCartButton.setOnClickListener(view -> {
            productData.removeFromCart(product);

            dismiss();
        });

        binding.purchaseButton.setOnClickListener(view -> {
            OrderDialogFragment fragment = new OrderDialogFragment(
                    new Order(LocalDate.now(),
                            MachineRepository.getInstance().getCurrentMachine(),
                            new ArrayList<>(Collections.singletonList(product))));

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .add(R.id.orderFragmentContainer, fragment, "order")
                    .addToBackStack("order")
                    .commit();

            dismiss();
        });

        binding.favoritesProductFavoriteCheckbox.setChecked(user.isProductFavorite(product));

        binding.favoritesProductFavoriteCheckbox.addOnCheckedStateChangedListener((checkBox, isChecked) -> {
            if(isChecked == MaterialCheckBox.STATE_CHECKED) {
                user.addFavorite(product);

                showSnackbar(binding.favoritesProductFavoriteCheckbox,
                        getString(R.string.cart_item_marked_alert, product.getName()),
                        binding.favoritesProductFavoriteCheckbox);
            }
            else {
                user.removeFavorite(product);

                showSnackbar(binding.favoritesProductFavoriteCheckbox,
                        getString(R.string.cart_item_unmarked_alert, product.getName()),
                        binding.favoritesProductFavoriteCheckbox);
            }
        });


        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_product_purchase_dialog, container, false);
    }
}