package com.monopalla.automat.ui.payment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentPaymentDialogBinding;
import com.monopalla.automat.databinding.OrderItemBinding;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.utils.AnimUtils;

import java.util.ArrayList;

public class OrderDialogFragment extends BottomSheetDialogFragment {
    private FragmentPaymentDialogBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    Order order;

    public OrderDialogFragment(Order order) {
        this.order = order;

        activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == 1) {
                    System.out.println("Hello");
                    getActivity().finish();
                }
            }
        );
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
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(new OrderItemAdapter(order.getItems()));
        binding.list.setNestedScrollingEnabled(false);

        binding.orderTotal.setText(getString(R.string.total_price, order.total()));
        binding.orderTotalInAutomats.setText(getString(R.string.total_price_in_automats, order.totalInAutomats()));

        binding.productPageAppBar.setNavigationOnClickListener(view1 -> {
            dismiss();
        });

        binding.payButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderCompleteActivity.class);

            UserRepository userData = UserRepository.getInstance(getContext());
            User user = userData.getCurrentUser();

            if (userData.isCurrentUserValid()) {

                user.addAutomats((int) order.earnedAutomats());
            }

            user.addToHistory(order);

            activityResultLauncher.launch(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()));

            dismiss();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
        private final ArrayList<Product> items;

        OrderItemAdapter(ArrayList<Product> items) {
            this.items = items;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            final TextView productName;
            final TextView productPrice;
            final ImageView productPic;
            final MaterialCheckBox checkBox;

            ViewHolder(OrderItemBinding binding) {
                super(binding.getRoot());
                productName = binding.orderProductName;
                productPrice = binding.orderProductPrice;
                productPic = binding.orderProductPic;
                checkBox = binding.orderProductSelect;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(OrderItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Product product = items.get(position);

            holder.productName.setText(product.getName());
            holder.productPrice.setText(getString(R.string.product_price, product.getPrice()));
            holder.productPic.setImageBitmap(product.getPicture());

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    boolean wasEmpty = order.getItems().isEmpty();

                    order.addItem(product);

                    if (wasEmpty) {
                        AnimUtils.switchViewsWithCircularReveal(binding.noItemsSelected, binding.orderTotalInfo);
                        binding.payButton.setEnabled(true);
                    }
                }
                else {
                    order.removeItem(product);

                    if (order.getItems().isEmpty()) {
                        AnimUtils.switchViewsWithCircularReveal(binding.orderTotalInfo, binding.noItemsSelected);
                        binding.payButton.setEnabled(false);
                    }
                }

                binding.orderTotal.setText(getString(R.string.total_price, order.total()));
                binding.orderTotalInAutomats.setText(getString(R.string.total_price_in_automats, order.totalInAutomats()));
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}