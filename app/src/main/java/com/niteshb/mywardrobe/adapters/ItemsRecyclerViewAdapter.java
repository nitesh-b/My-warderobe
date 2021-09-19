package com.niteshb.mywardrobe.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemModel;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ItemsRecyclerViewAdapter extends RealmRecyclerViewAdapter<ItemModel, ItemsRecyclerViewAdapter.MyViewHolder> {
    public static final String TAG = "ItemsRecyclerView";
    public ItemsRecyclerViewAdapter(@Nullable OrderedRealmCollection<ItemModel> data) {
        super(data, true, true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(getData()!= null && getData().size()> 0){
            ItemModel model = getData().get(position);
            holder.categoryTextView.setText(model.getCategory());
            holder.subCategoryTextView.setText(model.getSubCategory());
            holder.descriptionTextView.setText(model.getDescription());
            if(model.isFavourite()){
                holder.favouriteImage.setImageResource(R.drawable.heart_filled);
            }else{
                holder.favouriteImage.setImageResource(R.drawable.heart);
            }

            StorageReference reference = FirebaseStorage.getInstance().getReference(model.getImageReference());
            try{
                Glide.with(holder.itemImage.getContext())
                        .load(reference)
                        .into(holder.itemImage);

            }catch (Exception ex){
                Log.d(TAG, "onBindViewHolder: " + ex.getLocalizedMessage());
            }
        }



    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTextView, subCategoryTextView, descriptionTextView;
        private ImageView itemImage, favouriteImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.text_view_category);
            subCategoryTextView = itemView.findViewById(R.id.text_view_sub_category);
            descriptionTextView = itemView.findViewById(R.id.text_view_other_description);
            favouriteImage = itemView.findViewById(R.id.selected_item_layout_favourite);
            itemImage = itemView.findViewById(R.id.selected_item_image);

        }
    }
}
