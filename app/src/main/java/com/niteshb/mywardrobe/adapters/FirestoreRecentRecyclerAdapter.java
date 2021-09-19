package com.niteshb.mywardrobe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemModel;

public class FirestoreRecentRecyclerAdapter extends FirestoreRecyclerAdapter<ItemModel, FirestoreRecentRecyclerAdapter.MyViewHolder> {
    public Boolean open = false;
    public FirestoreRecentRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ItemModel model) {
        Glide.with(holder.itemView.getContext())
                .load(model.getImageReference())
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_items_layout, parent, false);
        return new MyViewHolder(view);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView colorTextView, typeTextView, storeTextView;
        private RatingBar myRating;
        private ImageView moreOption, edit, delete;
        private LinearLayout moreOptionLinearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moreOption = itemView.findViewById(R.id.more_option);
            moreOption.setOnClickListener((View.OnClickListener) this);
            moreOptionLinearLayout = itemView.findViewById(R.id.more_option_linear_layout);
            edit = itemView.findViewById(R.id.edit_item);
            edit.setOnClickListener((View.OnClickListener) this);
            delete = itemView.findViewById(R.id.delete_item);
            delete.setOnClickListener((View.OnClickListener) this);
            imageView = itemView.findViewById(R.id.recent_image);
            colorTextView = itemView.findViewById(R.id.recent_color_textview);
            typeTextView = itemView.findViewById(R.id.recent_type_textview);
            storeTextView = itemView.findViewById(R.id.recent_store_textview);
            myRating = itemView.findViewById(R.id.recent_rating_bar);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.more_option:
                    if(!open){
                        moreOptionLinearLayout.setVisibility(View.VISIBLE);
                        open = true;

                    }else{
                        moreOptionLinearLayout.setVisibility(View.GONE);
                        open = false;
                    }
                    break;

            }

        }
    }
}
