package com.niteshb.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.adapters.ItemsRecyclerViewAdapter;
import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;

import java.util.ArrayList;

import io.realm.RealmResults;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recentRecyclerView;
    private TextView toolbarTextViewTitle;
    private ImageButton mBackButton;
    private MaterialToolbar secondaryToolbar;
    private FloatingActionButton addItemButton;

    private ItemsRecyclerViewAdapter recentRecyclerAdapter;
    private RealmResults<ItemModel> itemModelRealmResults;
    private String subCategoryId;
    private String categoryId;
    private CategoryModel categoryModel;
    private SubCategoryModel subCategoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        subCategoryId = getIntent().getStringExtra("SUB_CATEGORY_ID");
        subCategoryModel = RealmHelper.getSubCategory(subCategoryId);
        itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId);

        secondaryToolbar = findViewById(R.id.top_toolbar);
        toolbarTextViewTitle = findViewById(R.id.toolbar_category);
        setSupportActionBar(secondaryToolbar);

        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recentRecyclerView = findViewById(R.id.category_recycler_view);
        initializeSubCategoryRecylerView();

        addItemButton = findViewById(R.id.floating_action_add_item);
        addItemButton.setOnClickListener(this);

    }

    private void initializeSubCategoryRecylerView() {
        if(subCategoryModel!=null){
            categoryModel = RealmHelper.getCategory(subCategoryModel.getCategoryId());
            toolbarTextViewTitle.setText(categoryModel.getCategory());
        }
        recentRecyclerAdapter = new ItemsRecyclerViewAdapter(itemModelRealmResults);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(this, 2);
        recentRecyclerView.setLayoutManager(layoutManager);
        recentRecyclerView.setAdapter(recentRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.floating_action_add_item):
                Intent intent = new Intent(this, AddItemActivity.class);
                intent.putExtra("SUB_CATEGORY_ID", subCategoryId);
                startActivity(intent);
                break;
        }
    }
}