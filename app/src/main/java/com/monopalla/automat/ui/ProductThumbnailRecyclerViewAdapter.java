package com.monopalla.automat.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.ProductThumbnailBinding;

import java.util.ArrayList;

public class ProductThumbnailRecyclerViewAdapter extends RecyclerView.Adapter<ProductThumbnailRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Product> products;

    public ProductThumbnailRecyclerViewAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView thumbnail;

        public ViewHolder(ProductThumbnailBinding binding) {
            super(binding.getRoot());

            thumbnail = binding.productThumbnail;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ProductThumbnailBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        System.out.println(product.getName());

        holder.thumbnail.setImageBitmap(product.getPicture());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
