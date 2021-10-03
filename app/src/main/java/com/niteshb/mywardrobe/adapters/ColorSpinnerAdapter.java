package com.niteshb.mywardrobe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemColorModel;
import com.niteshb.mywardrobe.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ColorSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemColorModel> objects;
    public ColorSpinnerAdapter(@NonNull Context context, ArrayList<ItemColorModel> objects) {
        this.context = context;
        this.objects = objects;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_select_color, parent, false);
        }
        ItemColorModel currentColor = objects.get(position);
        TextView textView = convertView.findViewById(R.id.textView_colorName);
        View colorView = convertView.findViewById(R.id.view_colorSample);
        if(currentColor != null){
            textView.setText(currentColor.getColorName());
        colorView.setBackgroundColor(Color.parseColor(currentColor.getHexCode()));
        }


        return convertView;
    }
}
