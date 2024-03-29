package com.monopalla.automat.ui.machine;

import static com.monopalla.automat.utils.UIUtils.showErrorSnackbar;
import static com.monopalla.automat.utils.UIUtils.showSnackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.data.model.MachineSlot;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.SlotProductItemBinding;
import com.monopalla.automat.ui.cart.CartDialogFragment;
import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.databinding.ActivityMachineBinding;
import com.monopalla.automat.utils.AnimUtils;
import com.monopalla.automat.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MachineActivity extends AppCompatActivity {
    ActivityMachineBinding binding;
    ProductRepository productData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMachineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        Machine machine = machineData.getCurrentMachine();
        productData = ProductRepository.getInstance(getApplicationContext());
        UserRepository userData = UserRepository.getInstance(getApplicationContext());

        binding.productList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.productList.setAdapter(new SlotRecyclerViewAdapter(
                machine.getSortedSlots(userData.getCurrentUser())));

        binding.machineAppBar.setNavigationOnClickListener(view -> {
            productData.clearCart();

            finishAfterTransition();
        });

        binding.appbarTitle.setText(machine.getName());

        binding.sortBy.setOnClickListener(v -> UIUtils.showNoActionSnackbar(
                binding.sortBy, binding.checkoutFAB
        ));

        binding.productCount.setText(getString(R.string.cart_product_count, machine.getProducts(userData.getCurrentUser()).size()));

        binding.checkoutFAB.setOnClickListener(view -> {
            if(productData.getCart().isCartEmpty()) {
                showErrorSnackbar(binding.checkoutFAB, getString(R.string.cart_empty_alert), binding.checkoutFAB);
                return;
            }

            CartDialogFragment cartBottomSheet = new CartDialogFragment(
                    productData.getCart());
            cartBottomSheet.show(getSupportFragmentManager(), "cart");
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        binding.checkoutFAB.setText(getString(R.string.cart_fab_text, productData.getCart().getItems().size()));
    }

    @Subscribe
    public void onAddedToCart(ProductRepository.AddedToCardEvent event) {
        binding.checkoutFAB.setText(getString(R.string.cart_fab_text, productData.getCart().getItems().size()));
    }

    @Subscribe
    public void onRemovedFromCart(ProductRepository.RemovedFromCartEvent event) {
        binding.checkoutFAB.setText(getString(R.string.cart_fab_text, productData.getCart().getItems().size()));
    }

    public class SlotRecyclerViewAdapter extends RecyclerView.Adapter<SlotRecyclerViewAdapter.ViewHolder> {
        private ArrayList<MachineSlot> slotList;

        public SlotRecyclerViewAdapter(ArrayList<MachineSlot> productList) {
            this.slotList = productList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final CardView productCardView;
            final TextView productNameTV;
            final TextView productPriceTV;
            final TextView productAvailabilityTV;
            final ImageView productPicIV;
            final ImageButton productAddToCartButton;
            final ImageButton productRemoveFromCartButton;
            final MaterialCheckBox productFavoriteCheckBox;

            public ViewHolder(SlotProductItemBinding binding) {
                super(binding.getRoot());

                productCardView = binding.productCard;
                productNameTV = binding.productName;
                productPriceTV = binding.productPrice;
                productAvailabilityTV = binding.productAvailability;
                productPicIV = binding.productPic;
                productAddToCartButton = binding.productAddToCartButton;
                productRemoveFromCartButton = binding.productRemoveFromCartButton;
                productFavoriteCheckBox = binding.productFavoriteCheckbox;
            }

            @Subscribe
            public void onAddedToCart(ProductRepository.AddedToCardEvent event) {
                if(productNameTV.getText().toString().compareTo(event.product.getName()) == 0) {
                    AnimUtils.switchViewsWithCircularReveal(
                            productAddToCartButton, productRemoveFromCartButton);

                    showSnackbar(productAddToCartButton,
                            productAddToCartButton.getContext()
                                    .getString(R.string.cart_item_added_alert, event.product.getName()),
                            binding.checkoutFAB);
                }
            }

            @Subscribe
            public void onRemovedFromCart(ProductRepository.RemovedFromCartEvent event) {
                if(productNameTV.getText().toString().compareTo(event.product.getName()) == 0) {
                    AnimUtils.switchViewsWithCircularReveal(
                            productRemoveFromCartButton, productAddToCartButton);

                    showSnackbar(productAddToCartButton,
                            productAddToCartButton.getContext()
                                    .getString(R.string.cart_item_removed_alert, event.product.getName()),
                            binding.checkoutFAB);
                }
            }

            @Subscribe
            public void onMarkedFavorite(User.MarkedFavoriteEvent event) {
                if(productNameTV.getText().toString().compareTo(event.product.getName()) == 0) {
                    productFavoriteCheckBox.setChecked(true);
                }
            }

            @Subscribe
            public void onMarkedFavorite(User.UnmarkedFavoriteEvent event) {
                if(productNameTV.getText().toString().compareTo(event.product.getName()) == 0) {
                    productFavoriteCheckBox.setChecked(false);
                }
            }
        }

        @NonNull
        @Override
        public SlotRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(SlotProductItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SlotRecyclerViewAdapter.ViewHolder holder, int position) {
            MachineSlot slot = slotList.get(position);
            Product product = slot.getProduct();
            Context context = holder.itemView.getContext();
            ProductRepository productData = ProductRepository.getInstance(context);
            UserRepository userData = UserRepository.getInstance(context);
            User user = userData.getCurrentUser();

            if (!EventBus.getDefault().isRegistered(holder)) {
                EventBus.getDefault().register(holder);
            }

            holder.productNameTV.setText(product.getName());
            holder.productPriceTV.setText(context.getString(R.string.product_price, product.getPrice()));
            holder.productAvailabilityTV.setText(context.getString(R.string.product_availability, slot.getNumberOfItems()));
            holder.productPicIV.setImageBitmap(product.getPicture());

            holder.productCardView.setOnClickListener(view -> {
                productData.setCurrentProduct(product);

                ProductPurchaseDialogFragment fragment = new ProductPurchaseDialogFragment(product);
                fragment.show(getSupportFragmentManager(), product.getName());
            });

            holder.productAddToCartButton.setOnClickListener(view -> {
                if(productData.getCart().isCartFull()) {
                    showErrorSnackbar(holder.productAddToCartButton,
                            context.getString(R.string.cart_full_alert),
                            binding.checkoutFAB);

                    return;
                }

                productData.addToCart(product);

                showSnackbar(holder.productAddToCartButton,
                        context.getString(R.string.cart_item_added_alert, product.getName()),
                        binding.checkoutFAB);
            });

            holder.productRemoveFromCartButton.setOnClickListener(view -> {
                productData.removeFromCart(product);

                showSnackbar(holder.productRemoveFromCartButton,
                        context.getString(R.string.cart_item_removed_alert, product.getName()),
                        binding.checkoutFAB);
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
                            context.getString(R.string.cart_item_marked_alert, product.getName()),
                            binding.checkoutFAB);
                }
                else {
                    user.removeFavorite(product);

                    showSnackbar(holder.productRemoveFromCartButton,
                            context.getString(R.string.cart_item_unmarked_alert, product.getName()),
                            binding.checkoutFAB);
                }

            });
        }

        @Override
        public int getItemCount() {
            return slotList.size();
        }
    }
}