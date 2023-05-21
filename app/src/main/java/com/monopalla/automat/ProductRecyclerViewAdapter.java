package com.monopalla.automat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.ProductRecyclerviewItemBinding;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Product> productList;

    public ProductRecyclerViewAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView productCardView;
        final TextView productNameTV;
        final TextView productPriceTV;
        final ImageView productPicIV;

        public ViewHolder(ProductRecyclerviewItemBinding binding) {
            super(binding.getRoot());

            productCardView = binding.productCard;
            productNameTV = binding.productName;
            productPriceTV = binding.productPrice;
            productPicIV = binding.productPic;
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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
