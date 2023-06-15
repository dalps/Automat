package com.monopalla.automat.ui.admin.machine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.R;
import com.monopalla.automat.data.MachineRepository;
import com.monopalla.automat.data.ProductRepository;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Machine;
import com.monopalla.automat.data.model.MachineSlot;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.ActivityInventoryBinding;
import com.monopalla.automat.databinding.InventoryItemBinding;
import com.monopalla.automat.ui.machine.ProductPurchaseDialogFragment;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {
    ActivityInventoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MachineRepository machineData = MachineRepository.getInstance(getApplicationContext());
        Machine machine = machineData.getCurrentMachine();
        ProductRepository productData = ProductRepository.getInstance(getApplicationContext());
        UserRepository userData = UserRepository.getInstance(getApplicationContext());

        binding.appbarTitle.setText(machine.getSerialNumber());

        binding.productList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.productList.setAdapter(new InventoryActivity.SlotRecyclerViewAdapter(
                machine.getSlotList()));

        binding.machineAppBar.setNavigationOnClickListener(v -> {
            finishAfterTransition();
        });

        binding.slotCount.setText(getString(R.string.slot_count, machine.getSortedSlots(userData.getCurrentUser()).size(), machine.getSlotList().size()));
    }

    public class SlotRecyclerViewAdapter extends RecyclerView.Adapter<InventoryActivity.SlotRecyclerViewAdapter.ViewHolder> {
        private ArrayList<MachineSlot> slotList;

        public SlotRecyclerViewAdapter(ArrayList<MachineSlot> slotList) {
            this.slotList = slotList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final CardView productCardView;
            final TextView productNameTV;
            final TextView productPriceTV;
            final TextView productAvailabilityTV;
            final ImageView productPicIV;
            final RelativeLayout assignedView;
            final RelativeLayout unassignedView;
            final TextView slotName;


            public ViewHolder(InventoryItemBinding binding) {
                super(binding.getRoot());

                productCardView = binding.productCard;
                productNameTV = binding.productName;
                productPriceTV = binding.productPrice;
                productAvailabilityTV = binding.productAvailability;
                productPicIV = binding.productPic;
                assignedView = binding.assignedSlotView;
                unassignedView = binding.unassignedSlotView;
                slotName = binding.slotName;
            }
        }

        @NonNull
        @Override
        public InventoryActivity.SlotRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new InventoryActivity.SlotRecyclerViewAdapter.ViewHolder(InventoryItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull InventoryActivity.SlotRecyclerViewAdapter.ViewHolder holder, int position) {
            MachineSlot slot = slotList.get(position);
            Product product = slot.getProduct();
            Context context = holder.itemView.getContext();
            ProductRepository productData = ProductRepository.getInstance(context);
            UserRepository userData = UserRepository.getInstance(context);
            User user = userData.getCurrentUser();

            holder.slotName.setText(getString(R.string.slot_name, slot.getName()));

            if (slot.isAssigned()) {
                holder.assignedView.setVisibility(View.VISIBLE);
                holder.unassignedView.setVisibility(View.GONE);

                holder.productNameTV.setText(product.getName());
                holder.productPriceTV.setText(context.getString(R.string.product_price, product.getPrice()));
                holder.productAvailabilityTV.setText(context.getString(R.string.product_availability, slot.getNumberOfItems()));
                holder.productPicIV.setImageBitmap(product.getPicture());

                holder.productCardView.setOnClickListener(view -> {
                    productData.setCurrentProduct(product);

                    AssignedSlotDialogFragment fragment = new AssignedSlotDialogFragment(slot);
                    fragment.show(getSupportFragmentManager(), slot.getName());
                });
            }
            else {
                holder.assignedView.setVisibility(View.GONE);
                holder.unassignedView.setVisibility(View.VISIBLE);

                holder.productCardView.setOnClickListener(view -> {
                    AssignSlotDialogFragment fragment = new AssignSlotDialogFragment(slot);
                    fragment.show(getSupportFragmentManager(), slot.getName());
                });
            }
        }

        @Override
        public int getItemCount() {
            return slotList.size();
        }

        public void showSnackbar(View view, String msg) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}