package com.monopalla.automat.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.monopalla.automat.data.UserRepository;
import com.monopalla.automat.data.model.Product;
import com.monopalla.automat.data.model.User;
import com.monopalla.automat.databinding.FavoriteItemBinding;

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

        if (user.anyFavorites()) {
            binding.noFavoriteMessage.setVisibility(View.INVISIBLE);

            binding.favoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.favoriesList.setAdapter(new FavoriteRecyclerViewAdapter(
                    new ArrayList<>(user.getFavoriteProducts())));
        }
        else {
            binding.noFavoriteMessage.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }
}
       

class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<Product> favorites;

    public FavoriteRecyclerViewAdapter(ArrayList<Product> favorites) {
        this.favorites = favorites;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

        holder.productName.setText(product.getName());
        holder.productPic.setImageBitmap(product.getPicture());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}