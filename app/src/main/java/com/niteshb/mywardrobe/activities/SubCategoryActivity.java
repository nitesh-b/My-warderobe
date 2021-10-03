package com.niteshb.mywardrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.adapters.ItemsRecyclerViewAdapter;
import com.niteshb.mywardrobe.adapters.SubCategoriesAdapter;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;
import com.niteshb.mywardrobe.utils.FlexibleGridView;

import java.util.ArrayList;

import io.realm.RealmResults;

public class SubCategoryActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {

    private RecyclerView recentRecyclerView;
    private FlexibleGridView subCategoryGridView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference itemCollection;
    private ItemsRecyclerViewAdapter recentRecyclerAdapter;
    private SubCategoriesAdapter subCategoriesAdapter;
    private FirebaseUser user;
    private String categoryId;

    private ImageButton mBackButton;
    private CategoryModel categoryModel;
    private ArrayList<SubCategoryModel> subCategoryModels;
    private RealmResults<ItemModel> itemModelRealmResults;
    private ItemClickListener itemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemClickListener = this;
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        categoryModel = RealmHelper.getCategory(categoryId);
        subCategoryModels = RealmHelper.getSubCategories(categoryId);

        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        subCategoryGridView = findViewById(R.id.gridView);
        if(subCategoryModels.size() == 0){
            finish();
            startItemActivity(null);
        }else{
            initSubCategoryGridView();
        }


        user = mAuth.getCurrentUser();
//        recentRecyclerView = findViewById(R.id.category_recycler_view);
//        initializeCategoryRecylerView();
    }

    private void initSubCategoryGridView() {

        subCategoriesAdapter = new SubCategoriesAdapter(subCategoryModels, itemClickListener);
        subCategoryGridView.setExpanded(true);
        subCategoryGridView.setAdapter(subCategoriesAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(Object item, int position) {
        if(item instanceof SubCategoryModel){
         startItemActivity(((SubCategoryModel) item).getId());
        }
    }

    private void startItemActivity(String subCategoryId){
        Intent intent = new Intent(SubCategoryActivity.this, ItemActivity.class);
        intent.putExtra("SUB_CATEGORY_ID", subCategoryId);
        intent.putExtra("CATEGORY_ID", categoryId);
        startActivity(intent);
    }
}