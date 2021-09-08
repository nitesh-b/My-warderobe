package com.niteshb.mywardrobe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {

    private List<String> dataList;
    private ItemClickListener itemClickListener;
//    private Context context;

    public CategoriesAdapter( List<String> dataList, ItemClickListener itemClickListener) {
//        this.context = context;
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
         CardView cardView;

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_categories, parent, false);
        }

        categoryTitle = convertView.findViewById(R.id.textView_category_title);
        cardView = convertView.findViewById(R.id.cardView);

        categoryTitle.setText(dataList.get(position));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(dataList.get(position), position);
            }
        });


        return convertView;
    }

}
