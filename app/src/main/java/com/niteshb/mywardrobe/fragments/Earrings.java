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
import com.niteshb.mywardrobe.activities.SelectedItemActivity;

import java.io.FileReader;

public class Earrings extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.earrings_fragment, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectedItemActivity.class);
                intent.putExtra("ITEM_MODEL", "Jwelleries");
                startActivity(intent);
            }
        });
        return view;
    }
}