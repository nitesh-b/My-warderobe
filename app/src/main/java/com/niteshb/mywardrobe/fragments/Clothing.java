package com.niteshb.mywardrobe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.activities.SubCategoryActivity;

public class Clothing extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLayoutView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clothing_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayoutView = view.findViewById(R.id.clothing_view);
        relativeLayoutView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.clothing_view:
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra("ITEM_MODEL", "Clothing");
                startActivity(intent);
                break;
        }
    }
}
