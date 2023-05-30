package com.monopalla.automat.ui.payment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.FragmentPaymentDialogCartItemBinding;
import com.monopalla.automat.databinding.FragmentPaymentDialogBinding;

import java.util.ArrayList;

public class PaymentDialogFragment extends BottomSheetDialogFragment {
    ArrayList<Product> cart;
    private FragmentPaymentDialogBinding binding;

    public PaymentDialogFragment(ArrayList<Product> cart) {
        this.cart = cart;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPaymentDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = binding.list;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new OrderAdapter(cart));
        recyclerView.setNestedScrollingEnabled(false);

        binding.paymentTotal.setText(getString(R.string.product_price,
                cart.stream().mapToDouble(Product::getPrice).sum()));

        binding.productPageAppBar.setNavigationOnClickListener(view1 -> {
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        final TextView productName;
        final TextView productPrice;
        final ImageView productPic;

        ViewHolder(FragmentPaymentDialogCartItemBinding binding) {
            super(binding.getRoot());
            productName = binding.productCheckOutName;
            productPrice = binding.productCheckOutPrice;
            productPic = binding.productCheckOutPic;
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<Product> cart;

        OrderAdapter(ArrayList<Product> cart) {
            this.cart = cart;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentPaymentDialogCartItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Product product = cart.get(position);

            holder.productName.setText(product.getName());
            holder.productPrice.setText(getString(R.string.product_price, product.getPrice()));
            holder.productPic.setImageBitmap(product.getPicture());
        }

        @Override
        public int getItemCount() {
            return cart.size();
        }
    }
}