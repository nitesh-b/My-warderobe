package com.niteshb.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.adapters.ItemsRecyclerViewAdapter;
import com.niteshb.mywardrobe.fragments.FilterDialogFragment;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.FilterModel;
import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;

import java.util.ArrayList;

import io.realm.RealmResults;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    private RecyclerView recentRecyclerView;
    private TextView toolbarTextViewTitle;
    private ImageButton mBackButton;
    private MaterialToolbar secondaryToolbar;
    private FloatingActionButton addItemButton;

    private Button clearButton;

    private ItemsRecyclerViewAdapter recentRecyclerAdapter;
    private RealmResults<ItemModel> itemModelRealmResults;
    private String subCategoryId;
    private String categoryId;
    private CategoryModel categoryModel;
    private SubCategoryModel subCategoryModel;
    private ItemClickListener itemClickListener;
    private FilterDialogFragment filterDialogFragment;
    private boolean isFilteredView;
    private boolean favouriteView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        itemClickListener = this;
        subCategoryId = getIntent().getStringExtra("SUB_CATEGORY_ID");
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        favouriteView = getIntent().getBooleanExtra("FAVOURITE", false);
        categoryModel = RealmHelper.getCategory(categoryId);
        subCategoryModel = RealmHelper.getSubCategory(subCategoryId);
        if(favouriteView){
            if(subCategoryId == null){
                itemModelRealmResults = RealmHelper.getCategoryItem(categoryId, favouriteView);
            }else{
                itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId, favouriteView);
            }
        }else{
            if(subCategoryId == null){
                itemModelRealmResults = RealmHelper.getCategoryItem(categoryId, favouriteView);
            }else{
                itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId, favouriteView);
            }
        }





        secondaryToolbar = findViewById(R.id.secondary_toolbar);
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

        clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener(this);
        findViewById(R.id.filter_layout_holder).setVisibility(View.GONE);

    }

    private void initializeSubCategoryRecylerView() {
        if(subCategoryModel!=null){
            toolbarTextViewTitle.setText(subCategoryModel.getSubCategory());
        }else{
            if(categoryModel!=null){
                toolbarTextViewTitle.setText(categoryModel.getCategory());
            }
        }
        recentRecyclerAdapter = new ItemsRecyclerViewAdapter(itemModelRealmResults, itemClickListener);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(this, 2);
        recentRecyclerView.setLayoutManager(layoutManager);
        recentRecyclerView.setAdapter(recentRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secondary_common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.secondary_menu_search):
                boolean setSelected = !favouriteView;
                if(setSelected) {
                    item.setIcon(R.drawable.heart_filled);
                    favouriteView = true;
                    if (subCategoryId == null) {
                        itemModelRealmResults = RealmHelper.getCategoryItem(categoryId, true);
                    } else {
                        itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId, true);
                    }

                }else{
                    item.setIcon(R.drawable.heart);
                    favouriteView = false;
                    if (subCategoryId == null) {
                        itemModelRealmResults = RealmHelper.getCategoryItem(categoryId, false);
                    } else {
                        itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId, false);
                    }
                }
                recentRecyclerAdapter.updateData(itemModelRealmResults);
                break;
            case (R.id.secondary_menu_filter):
                filterDialogFragment = new FilterDialogFragment(itemClickListener, RealmHelper.getSubTypes(subCategoryId));
                filterDialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.floating_action_add_item):
                Intent intent = new Intent(this, AddItemActivity.class);
                intent.putExtra("SUB_CATEGORY_ID", subCategoryId);
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivity(intent);
                break;

            case (R.id.button_clear):
                if(subCategoryId == null){
                    itemModelRealmResults = RealmHelper.getCategoryItem(categoryId, favouriteView);
                }else{
                    itemModelRealmResults = RealmHelper.getSubCategoryItem(subCategoryId, favouriteView);
                }
                recentRecyclerAdapter.updateData(itemModelRealmResults);
                findViewById(R.id.filter_layout_holder).setVisibility(View.GONE);

        }
    }

    @Override
    public void onItemClick(Object item, int position) {
        if(item instanceof ItemModel){
            ItemModel  model = (ItemModel) item;
            Intent intent = new Intent(ItemActivity.this, DetailItemActivity.class);
            intent.putExtra("ITEM_ID", model.getId());
            startActivity(intent);
        }else if (item instanceof FilterModel){
            FilterModel filterModel = (FilterModel) item;
            filterItems(filterModel);
        }
    }

    private void filterItems(FilterModel filterModel) {
       // Toast.makeText(this, "Filter success", Toast.LENGTH_SHORT).show();
        findViewById(R.id.filter_layout_holder).setVisibility(View.VISIBLE);
        isFilteredView = true;
        if(filterModel != null){
            itemModelRealmResults = RealmHelper.getFilteredItems(categoryId, subCategoryId, filterModel);
            recentRecyclerAdapter.updateData(itemModelRealmResults);

        }


    }
}