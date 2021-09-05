package com.niteshb.mywardrobe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemModel;

public class SelectedIItemRecyclerAdapter extends FirestoreRecyclerAdapter<ItemModel, SelectedIItemRecyclerAdapter.MyViewHolder> {

    public SelectedIItemRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ItemModel model) {
        holder.colorTextView.setText(model.getColor());
        holder.typeTextView.setText(model.getType());
        holder.myRating.setRating(model.getRating());
        holder.priceTextView.setText(String.format("$%d", model.getPrice()));
        holder.sizeTextView.setText(model.getSize());
        Glide.with(holder.itemView)
                .load(model.getImageReference())
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView, favouriteImageView;
        private TextView colorTextView, typeTextView, priceTextView, sizeTextView;
        private RatingBar myRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.selected_item_image);
            colorTextView = itemView.findViewById(R.id.selected_item_layout_color);
            typeTextView = itemView.findViewById(R.id.selected_item_layout_type);
            myRating = itemView.findViewById(R.id.selected_item_layout_rating);
            priceTextView = itemView.findViewById(R.id.selected_item_layout_price);
            favouriteImageView = itemView.findViewById(R.id.selected_item_layout_favourite);
            sizeTextView = itemView.findViewById(R.id.selected_item_layout_size);
        }
    }
}
