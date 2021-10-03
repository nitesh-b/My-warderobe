package com.niteshb.mywardrobe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;

import java.util.ArrayList;

public class SubCategoriesAdapter extends BaseAdapter {

    private ArrayList<SubCategoryModel> dataList;
    private ItemClickListener itemClickListener;

    public SubCategoriesAdapter(ArrayList<SubCategoryModel> dataList, ItemClickListener itemClickListener) {
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
         TextView subCategoryTitle;
         LinearLayout cardView;

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_categories, parent, false);
        }

        subCategoryTitle = convertView.findViewById(R.id.textView_category_title);
        cardView = convertView.findViewById(R.id.cardView);

        subCategoryTitle.setText(dataList.get(position).getSubCategory());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(dataList.get(position), position);
            }
        });


        return convertView;
    }

}
