package com.monopalla.automat.ui.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.monopalla.automat.R;
import com.monopalla.automat.data.model.Cart;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.databinding.CartItemBinding;
import com.monopalla.automat.databinding.FragmentCartDialogBinding;
import com.monopalla.automat.ui.payment.OrderDialogFragment;

public class CartDialogFragment extends BottomSheetDialogFragment {
    Cart cart;
    FragmentCartDialogBinding binding;

    public CartDialogFragment(Cart cart) {
        this.cart = cart;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = binding.cartRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CartItemAdapter(cart));
        recyclerView.setNestedScrollingEnabled(false);

        binding.payButton.setOnClickListener(view1 -> {
            OrderDialogFragment fragment = new OrderDialogFragment(cart.toOrder());

            fragment.show(getParentFragmentManager(), "payment");
            dismiss();
        });

        binding.cartAppBar.setNavigationOnClickListener(view1 -> dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
        private final Cart cart;

        CartItemAdapter(Cart cart) {
            this.cart = cart;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            final TextView productName, productPrice, itemQuantity;
            final ImageView productPic;
            final ImageButton addUnitButton, removeUnitButton;

            ViewHolder(CartItemBinding binding) {
                super(binding.getRoot());
                productName = binding.cartProductName;
                productPrice = binding.cartProductPrice;
                productPic = binding.cartProductPic;
                addUnitButton = binding.addUnitButton;
                removeUnitButton = binding.removeUnitButton;
                itemQuantity = binding.itemQuantity;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(CartItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Cart.CartItem item = cart.getItem(position);
            Product product = item.getProduct();

            holder.productName.setText(product.getName());
            holder.productPrice.setText(getString(R.string.product_price, product.getPrice()));
            holder.itemQuantity.setText(getString(R.string.item_quantity, item.getQuantity()));
            holder.productPic.setImageBitmap(product.getPicture());

            holder.addUnitButton.setOnClickListener(view -> {
                cart.increaseItemQuanitity(position);
                holder.itemQuantity.setText(getString(R.string.item_quantity, item.getQuantity()));
            });

            holder.removeUnitButton.setOnClickListener(view -> {
                cart.decreaseItemQuanitity(position);
                holder.itemQuantity.setText(getString(R.string.item_quantity, item.getQuantity()));
            });
        }

        @Override
        public int getItemCount() {
            return cart.getItems().size();
        }
    }

}