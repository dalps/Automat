package com.monopalla.automat.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.monopalla.automat.R;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FavoriteItemBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.View;
import com.monopalla.automat.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {
    FragmentFavoritesBinding binding;

    public FavoritesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        UserRepository userData = UserRepository.getInstance(getContext());
        User user = userData.getCurrentUser();
        ArrayList<Product> favorites = user.getFavoriteProducts();

        if (user.anyFavorites()) {
            binding.noFavoriteMessage.setVisibility(View.INVISIBLE);

            binding.favoriesList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            binding.favoriesList.setAdapter(new FavoriteRecyclerViewAdapter(favorites));
        }
        else {
            binding.noFavoriteMessage.setVisibility(View.VISIBLE);
        }

        binding.favoritesCount.setText(getString(R.string.cart_product_count, favorites.size()));

        return binding.getRoot();
    }

    class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Product> favorites;

        public FavoriteRecyclerViewAdapter(ArrayList<Product> favorites) {
            this.favorites = favorites;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView productName;
            final ImageView productPic;
            final MaterialCheckBox markAsFavorite;

            public ViewHolder(FavoriteItemBinding binding) {
                super(binding.getRoot());

                productName = binding.favoritesProductName;
                productPic = binding.favoritesProductPic;
                markAsFavorite = binding.favoritesProductFavoriteCheckbox;
            }
        }

        @NonNull
        @Override
        public FavoriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FavoriteItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteRecyclerViewAdapter.ViewHolder holder, int position) {
            Product product = favorites.get(position);
            Context context = holder.itemView.getContext();
            User user = UserRepository.getInstance(context).getCurrentUser();

            holder.productName.setText(product.getName());
            holder.productPic.setImageBitmap(product.getPicture());

            holder.markAsFavorite.addOnCheckedStateChangedListener((checkBox, isChecked) -> {
                if(isChecked == MaterialCheckBox.STATE_CHECKED) {
                    user.addFavorite(product);

                    showSnackbar(holder.markAsFavorite,
                            context.getString(R.string.cart_item_marked_alert, product.getName()));
                }
                else {
                    user.removeFavorite(product);

                    showSnackbar(holder.markAsFavorite,
                            context.getString(R.string.cart_item_unmarked_alert, product.getName()));
                }

                binding.favoritesCount.setText(getString(R.string.cart_product_count, favorites.size()));
            });
        }

        @Override
        public int getItemCount() {
            return favorites.size();
        }

        public void showSnackbar(View view, String msg) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAnchorView(getActivity().findViewById(R.id.bottomNav))
                .show();
        }
    }
}