package com.monopalla.automat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.model.Product;

import java.util.ArrayList;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Product> productList;
    private ProductRepository productData;
    private Context context;

    public ProductRecyclerViewAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView productCardView;
        private final TextView productNameTV;
        private final TextView productPriceTV;
        private final ImageView productPicIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productCardView = itemView.findViewById(R.id.productCard);
            productNameTV = itemView.findViewById(R.id.productName);
            productPriceTV = itemView.findViewById(R.id.productPrice);
            productPicIV = itemView.findViewById(R.id.productPic);
        }


        public CardView getProductCardView() {
            return productCardView;
        }
        public TextView getProductNameTV() {
            return productNameTV;
        }
        public TextView getProductPriceTV() {
            return productPriceTV;
        }
        public ImageView getProductPicIV() {
            return productPicIV;
        }
    }

    @NonNull
    @Override
    public ProductRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerViewAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        Resources res = holder.itemView.getResources();

         productData = ProductRepository.getInstance(context);

        CardView productCardView = holder.getProductCardView();
        TextView productNameTV = holder.getProductNameTV();
        TextView productPriceTV = holder.getProductPriceTV();
        ImageView productPicIV = holder.getProductPicIV();

        productPicIV.setTransitionName("productPic" + position);
        /* productNameTV.setTransitionName("productName" + position);
        productPriceTV.setTransitionName("productPrice" + position); */

        productNameTV.setText(product.getName());
        String price = context.getString(R.string.product_price, product.getPrice());
        productPriceTV.setText(price);
        productPicIV.setImageBitmap(product.getPicture());

        productCardView.setOnClickListener(view -> {
            productData.setCurrentProduct(product);

            Intent intent = new Intent(context, ProductActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context
            );

            intent.putExtra("TRANSITION_KEY", position);
            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
