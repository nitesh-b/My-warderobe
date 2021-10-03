package com.niteshb.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.realmHelper.EditRealmHelper;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;

import io.realm.ObjectChangeSet;
import io.realm.RealmObjectChangeListener;

public class DetailItemActivity extends AppCompatActivity {
    private TextView categoryTextView, subCategoryTextView, descriptionTextView;
    private ImageView itemImage, favouriteImage;
    private ImageButton backButton;

    private TextView toolbarTextViewTitle;
    private MaterialToolbar secondaryToolbar;

    private ItemModel itemModel;

    private final RealmObjectChangeListener<ItemModel> itemModelRealmObjectChangeListener = new RealmObjectChangeListener<ItemModel>() {
        @Override
        public void onChange(ItemModel model, @Nullable ObjectChangeSet changeSet) {
            setValues(itemModel);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        initView();
        String itemId = getIntent().getStringExtra("ITEM_ID");
        itemModel = RealmHelper.getItem(itemId);
        if(itemModel != null){
            itemModel.addChangeListener(itemModelRealmObjectChangeListener);
            setValues(itemModel);
        }
    }

    private void initView() {
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        categoryTextView = findViewById(R.id.text_view_category);
        subCategoryTextView = findViewById(R.id.text_view_sub_category);
        descriptionTextView = findViewById(R.id.text_view_other_description);
        favouriteImage = findViewById(R.id.selected_item_layout_favourite);
        itemImage = findViewById(R.id.photo_view);
        secondaryToolbar = findViewById(R.id.top_toolbar);
        toolbarTextViewTitle = findViewById(R.id.toolbar_category);
        setSupportActionBar(secondaryToolbar);
    }

    private void setValues(ItemModel model) {
        categoryTextView.setText(model.getCategory());
        subCategoryTextView.setText(model.getSubCategory());
        descriptionTextView.setText(model.getDescription());
        toolbarTextViewTitle.setText("Image");
        if(model.isFavourite()){
            favouriteImage.setImageResource(R.drawable.heart_filled);
        }else{
            favouriteImage.setImageResource(R.drawable.heart);
        }

        // StorageReference reference = FirebaseStorage.getInstance().getReference(model.getImageReference());
        try{
            Glide.with(itemImage.getContext())
                    .load(model.getImageReference())
                    //  .load(reference)
                    .into(itemImage);

        }catch (Exception ex){
            Log.d("TAG", "onBindViewHolder: " + ex.getLocalizedMessage());
        }
        favouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditRealmHelper.setFavourite(model.getId(), !model.isFavourite());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.detail_menu_edit):
                Toast.makeText(this, "Search for item", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}