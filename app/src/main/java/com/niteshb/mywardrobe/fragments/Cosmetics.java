package com.niteshb.mywardrobe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.activities.SubCategoryActivity;

public class Cosmetics extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cosmetics_fragment, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra("ITEM_MODEL", "Cosmetics");
                startActivity(intent);
            }
        });
        return view;
    }
}
