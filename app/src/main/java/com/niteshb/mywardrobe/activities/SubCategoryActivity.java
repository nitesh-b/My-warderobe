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
    private TextView toolbarTextViewTitle;
    private ImageButton mBackButton;
    private CategoryModel categoryModel;
    private ArrayList<SubCategoryModel> subCategoryModels;
    private RealmResults<ItemModel> itemModelRealmResults;
    private ItemClickListener itemClickListener;


    private MaterialToolbar secondaryToolbar;
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

        secondaryToolbar = findViewById(R.id.top_toolbar);
        toolbarTextViewTitle = findViewById(R.id.toolbar_category);
        setSupportActionBar(secondaryToolbar);

        toolbarTextViewTitle.setText(categoryModel.getCategory());
        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        subCategoryGridView = findViewById(R.id.gridView);
        initSubCategoryGridView();

        user = mAuth.getCurrentUser();
        recentRecyclerView = findViewById(R.id.category_recycler_view);
        initializeCategoryRecylerView();
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

    private void initializeCategoryRecylerView() {
        itemModelRealmResults = RealmHelper.getCategoryItem(categoryId);
        recentRecyclerAdapter = new ItemsRecyclerViewAdapter(itemModelRealmResults);
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
                Toast.makeText(this, "Search for item", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.secondary_menu_filter):
                    Toast.makeText(this, "OPen Filter View", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

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
            Intent intent = new Intent(SubCategoryActivity.this, ItemActivity.class);
            intent.putExtra("SUB_CATEGORY_ID", ((SubCategoryModel) item).getId());
            startActivity(intent);
        }
    }
}