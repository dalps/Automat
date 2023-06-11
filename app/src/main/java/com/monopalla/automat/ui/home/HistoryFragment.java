package com.monopalla.automat.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Order;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FragmentHistoryBinding;
import com.monopalla.automat.databinding.OrderHistoryItemBinding;
import com.monopalla.automat.ui.ProductThumbnailRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    FragmentHistoryBinding binding;

    public HistoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        UserRepository userData = UserRepository.getInstance(getContext());
        User user = userData.getCurrentUser();

        if (user.anyOrders()) {
            binding.noOrdersMessage.setVisibility(View.INVISIBLE);
            binding.orderHistorySection.setVisibility(View.VISIBLE);

            binding.orderHistoryLength.setText(getString(R.string.order_history_length_message, user.historyLength()));

            binding.ordersList.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.ordersList.setAdapter(new OrderRecyclerViewAdapter(user.getOrderHistory()));
        }
        else {
            binding.noOrdersMessage.setVisibility(View.VISIBLE);
            binding.orderHistorySection.setVisibility(View.INVISIBLE);
        }

        return binding.getRoot();
    }


    class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Order> orders;

        public OrderRecyclerViewAdapter(ArrayList<Order> orders) {
            this.orders = orders;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(OrderHistoryItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Order order = orders.get(position);

            holder.orderDate.setText(order.getDate().toString());
            holder.orderMachine.setText(order.getMachine().getName());
            holder.orderTotal.setText(getString(R.string.product_price, order.total()));

            holder.orderItems.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            holder.orderItems.setAdapter(new ProductThumbnailRecyclerViewAdapter(order.getItems()));
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView orderDate;
            final TextView orderTotal;
            final TextView orderMachine;
            final RecyclerView orderItems;

            public ViewHolder(OrderHistoryItemBinding binding) {
                super(binding.getRoot());

                orderDate = binding.orderHistoryDate;
                orderTotal = binding.orderHistoryTotal;
                orderMachine = binding.orderHistoryMachine;
                orderItems = binding.orderHistoryItems;
            }
        }


    }
}

