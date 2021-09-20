package com.niteshb.mywardrobe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.adapters.CategoriesAdapter;
import com.niteshb.mywardrobe.adapters.SectionPagerAdapter;
import com.niteshb.mywardrobe.fragments.Bags;
import com.niteshb.mywardrobe.fragments.Clothing;
import com.niteshb.mywardrobe.fragments.Cosmetics;
import com.niteshb.mywardrobe.fragments.Earrings;
import com.niteshb.mywardrobe.fragments.Shoes;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;
import com.niteshb.mywardrobe.services.MyService;
import com.niteshb.mywardrobe.utils.FlexibleGridView;
import com.niteshb.mywardrobe.viewmodels.FirebaseItemViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FlexibleGridView gridView;
    private CategoriesAdapter categoriesAdapter;
    private ArrayList<CategoryModel> categoriesList;
    private MaterialToolbar materialToolbar;

    private FirebaseItemViewModel itemViewModel;

    private MyService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemViewModel = new ViewModelProvider(this).get(FirebaseItemViewModel.class);
        itemViewModel.getMyBinder().observe(this, new Observer<MyService.MyBinder>() {
            @Override
            public void onChanged(MyService.MyBinder myBinder) {
                if(myBinder != null){
                    mService = myBinder.getMyService();
                    mService.performTask();
                }else {
                    mService = null;
                }
            }
        });

        setUpGridView();

        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setTitle("");
        mAuth = FirebaseAuth.getInstance();
 //       mAddButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }


    private void startService() {
        Log.d(TAG, "Fireservice: Firestore read service started" );
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        bindService();
    }
    private  void bindService(){
        Log.d(TAG, "Fireservice: Firestore read service bounded" );
        Intent serviceIntent = new Intent(this, MyService.class);
        bindService(serviceIntent, itemViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);

    }

    private void setUpGridView() {
        categoriesList = RealmHelper.getCategories();

        gridView = findViewById(R.id.flexible_grid_view);
        categoriesAdapter = new CategoriesAdapter(categoriesList, this);
        gridView.setExpanded(true);
        gridView.setAdapter(categoriesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                Toast.makeText(this, "Menu Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_signout:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager mViewPager) {
        SectionPagerAdapter viewPagerAapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPagerAapter.addFragment(new Clothing());
        viewPagerAapter.addFragment(new Bags());
        viewPagerAapter.addFragment(new Cosmetics());
        viewPagerAapter.addFragment(new Earrings());
        viewPagerAapter.addFragment(new Shoes());
        mViewPager.setAdapter(viewPagerAapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.signout_button:
                Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
                break;*/
//            case R.id.add_item_floating_button:
//                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
//                break;
        }

    }

    @Override
    public void onBackPressed() {
       mAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, Login.class));
        Toast.makeText(MainActivity.this, "Sign In to continue...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(Object item, int position) {
        if(item instanceof CategoryModel){
            startSelectedItemActivity((CategoryModel)item);
        }
    }

    private void startSelectedItemActivity(CategoryModel item) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("CATEGORY_ID", item.getId());
        startActivity(intent);
    }

}
