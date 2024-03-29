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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.R;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentOrderBinding;
import com.monopalla.automat.databinding.OrderItemBinding;
import com.monopalla.automat.ui.user.LoginFragment;
import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.utils.UIUtils;

import java.util.ArrayList;

public class OrderDialogFragment extends Fragment {
    private FragmentOrderBinding binding;
    Order order;

    public OrderDialogFragment(Order order) {
        this.order = order;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(new OrderItemAdapter(order.getItems()));
        binding.list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.list.setNestedScrollingEnabled(true);

        ProductRepository productData = ProductRepository.getInstance(getContext());
        UserRepository userData = UserRepository.getInstance(getContext());
        User user = userData.getCurrentUser();

        binding.orderTotal.setText(getString(R.string.total_price, order.total()));
        binding.itemsTitle.setText(getString(R.string.order_items_count, order.getItems().size()));
        binding.orderTotalInAutomats.setText(getString(R.string.total_price_in_automats, order.totalInAutomats()));

        binding.productPageAppBar.setNavigationOnClickListener(view1 -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .remove(this)
                    .commit();
        });

        if (user.getAutomats() >= order.totalInAutomats()) {
            binding.sufficientPoints.setVisibility(View.VISIBLE);
            binding.methodAutomatPoints.setVisibility(View.VISIBLE);

            binding.methodsRadioGroup.check(R.id.methodAutomatPoints);
        }
        else {
            binding.sufficientPoints.setVisibility(View.GONE);
            binding.methodAutomatPoints.setVisibility(View.GONE);

            binding.methodsRadioGroup.check(R.id.methodGooglePay);
        }

        binding.payButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderCompleteActivity.class);

            if (order.getItems().isEmpty()) {
                UIUtils.showErrorSnackbar(
                        binding.payButton, "Seleziona almeno un articolo!", binding.payButton
                );

                return;
            }

            if (binding.methodsRadioGroup.getCheckedRadioButtonId() == R.id.methodAutomatPoints) {
                if (userData.isCurrentUserValid()) {
                    user.spendAutomats((int) order.totalInAutomats());
                }

                order.setPaymentMethod(Order.POINTS_METHOD);
            }
            else {
                order.setPaymentMethod(Order.OTHER_METHOD);
            }

            user.addToHistory(order);
            productData.setCurrentOrder(order);

            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());

            // dismiss();
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

            holder.itemView.setForeground(null);

            holder.productName.setText(product.getName());
            holder.productPrice.setText(getString(R.string.product_price, product.getPrice()));
            holder.productPic.setImageBitmap(product.getPicture());

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    boolean wasEmpty = order.getItems().isEmpty();

                    order.addItem(product);

                    if (wasEmpty) {
                        AnimUtils.switchViewsWithCircularReveal(binding.noItemsSelected, binding.orderTotalInfo);
                    }
                }
                else {
                    order.removeItem(product);

                    if (order.getItems().isEmpty()) {
                        AnimUtils.switchViewsWithCircularReveal(binding.orderTotalInfo, binding.noItemsSelected);
                    }
                }

                binding.orderTotal.setText(getString(R.string.total_price, order.total()));
                binding.itemsTitle.setText(getString(R.string.order_items_count, order.getItems().size()));
                binding.orderTotalInAutomats.setText(getString(R.string.total_price_in_automats, order.totalInAutomats()));
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}