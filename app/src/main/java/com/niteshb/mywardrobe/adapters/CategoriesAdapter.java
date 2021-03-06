package com.niteshb.mywardrobe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.constants.Constants;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private ArrayList<CategoryModel> dataList;
    private ItemClickListener itemClickListener;

    public CategoriesAdapter(ArrayList<CategoryModel> dataList, ItemClickListener itemClickListener) {
        this.dataList = dataList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         TextView categoryTitle;
         LinearLayout cardView;
        ImageView backgroundImage;

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_categories, parent, false);
        }

        categoryTitle = convertView.findViewById(R.id.textView_category_title);
        cardView = convertView.findViewById(R.id.cardView);
        backgroundImage = convertView.findViewById(R.id.imageView);
        backgroundImage.setImageDrawable(ContextCompat.getDrawable(parent.getContext(), Constants.getCategoryImage(dataList.get(position).getCategory())));
        categoryTitle.setText(dataList.get(position).getCategory());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(dataList.get(position), position);
            }
        });


        return convertView;
    }

}
