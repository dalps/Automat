package com.monopalla.automat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ProductRecyclerviewItemBinding;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Product> productList;
    View snackBarAnchor;

    public ProductRecyclerViewAdapter(ArrayList<Product> productList, View snackBarAnchor) {
        this.productList = productList;
        this.snackBarAnchor = snackBarAnchor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView productCardView;
        final TextView productNameTV;
        final TextView productPriceTV;
        final ImageView productPicIV;
        final ImageButton productAddToCartButton;
        final ImageButton productRemoveFromCartButton;
        final MaterialCheckBox productFavoriteCheckBox;

        public ViewHolder(ProductRecyclerviewItemBinding binding) {
            super(binding.getRoot());

            productCardView = binding.productCard;
            productNameTV = binding.productName;
            productPriceTV = binding.productPrice;
            productPicIV = binding.productPic;
            productAddToCartButton = binding.productAddToCartButton;
            productRemoveFromCartButton = binding.productRemoveFromCartButton;
            productFavoriteCheckBox = binding.productFavoriteCheckbox;
        }
    }

    @NonNull
    @Override
    public ProductRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(ProductRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        Context context = holder.itemView.getContext();
        ProductRepository productData = ProductRepository.getInstance(context);
        UserRepository userData = UserRepository.getInstance(context);
        User user = userData.getCurrentUser();

        holder.productNameTV.setText(product.getName());
        holder.productPriceTV.setText(context.getString(R.string.product_price, product.getPrice()));
        holder.productPicIV.setImageBitmap(product.getPicture());

        holder.productCardView.setOnClickListener(view -> {
            productData.setCurrentProduct(product);

            Intent intent = new Intent(context, ProductActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context);

            context.startActivity(intent, options.toBundle());
        });

        holder.productAddToCartButton.setOnClickListener(view -> {
            if(productData.getCart().isCartFull()) {
                showSnackbar(holder.productAddToCartButton,
                        context.getString(R.string.cart_full_alert));

                return;
            }

            productData.addToCart(product);

            showSnackbar(holder.productAddToCartButton,
                    context.getString(R.string.cart_item_added_alert, product.getName()));

            AnimUtils.switchViewsWithCircularReveal(
                    holder.productAddToCartButton, holder.productRemoveFromCartButton);
        });

        holder.productRemoveFromCartButton.setOnClickListener(view -> {
            productData.getCart().removeProduct(product);

            showSnackbar(holder.productRemoveFromCartButton,
                    context.getString(R.string.cart_item_removed_alert, product.getName()));

            AnimUtils.switchViewsWithCircularReveal(
                    holder.productRemoveFromCartButton, holder.productAddToCartButton);
        });

        if (user.isProductFavorite(product)) {
            holder.productFavoriteCheckBox.setChecked(true);
        }
        else {
            holder.productFavoriteCheckBox.setChecked(false);
        }

        holder.productFavoriteCheckBox.addOnCheckedStateChangedListener((checkBox, isChecked) -> {
            if(isChecked == MaterialCheckBox.STATE_CHECKED) {
                user.addFavorite(product);

                showSnackbar(holder.productRemoveFromCartButton,
                        context.getString(R.string.cart_item_marked_alert, product.getName()));
            }
            else {
                user.removeFavorite(product);

                showSnackbar(holder.productRemoveFromCartButton,
                                context.getString(R.string.cart_item_unmarked_alert, product.getName()));
            }

        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void showSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAnchorView(snackBarAnchor)
                .show();
    }
}
