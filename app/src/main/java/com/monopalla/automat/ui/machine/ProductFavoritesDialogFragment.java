package com.monopalla.automat.ui.machine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentProductFavoritesDialogBinding;
import com.monopalla.automat.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

public class ProductFavoritesDialogFragment extends DialogFragment {
    FragmentProductFavoritesDialogBinding binding;
    Product product;

    public ProductFavoritesDialogFragment(Product product) {
        this.product = product;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        User user = UserRepository.getInstance(getContext()).getCurrentUser();

        binding = FragmentProductFavoritesDialogBinding.inflate(getLayoutInflater());

        binding.productPageTitle.setText(getString(R.string.details_title, product.getName()));

        binding.productPagePic.setImageBitmap(product.getPicture());
        binding.productPageName.setText(product.getName());
        binding.productPageDescription.setText(product.getDescription());

        binding.productPageAppBar.setNavigationOnClickListener(v -> {
            dismiss();
        });
        
        binding.share.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.share, binding.share
        ));
        
        binding.removeFromFavorites.setOnClickListener(v -> {
            user.removeFavorite(product);

            dismiss();
        });

        builder.setView(binding.getRoot());

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return inflater.inflate(R.layout.fragment_product_favorites_dialog, container, false);
    }
}