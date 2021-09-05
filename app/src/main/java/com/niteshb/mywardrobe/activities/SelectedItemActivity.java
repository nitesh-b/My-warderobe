package com.niteshb.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.adapters.SelectedIItemRecyclerAdapter;
import com.niteshb.mywardrobe.models.ItemModel;

public class SelectedItemActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference itemCollection;
    private SelectedIItemRecyclerAdapter recentRecyclerAdapter;
    private FirebaseUser user;
    private String intentString;
    private TextView toolbarTextViewTitle;
    private ImageButton mBackButton;

    private MaterialToolbar secondaryToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        intentString = getIntent().getStringExtra("ITEM_MODEL");

        secondaryToolbar = findViewById(R.id.top_toolbar);
        toolbarTextViewTitle = findViewById(R.id.toolbar_category);
        setSupportActionBar(secondaryToolbar);

        toolbarTextViewTitle.setText(intentString);
        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        user = mAuth.getCurrentUser();
        mRecyclerView = findViewById(R.id.category_recycler_view);
        populateFilterView();
        itemCollection = db.collection("My-Wardrobe").document("Users").collection(user.getEmail());
        initializeCategoryRecylerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeCategoryRecylerView() {
        Query query =  itemCollection.orderBy("itemNumber", Query.Direction.DESCENDING).whereEqualTo("category", intentString);
        FirestoreRecyclerOptions<ItemModel> option = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class)
                .build();
        handleQurey(query, option);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secondary_common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.secondary_menu_search:
                Toast.makeText(this, "Search for item", Toast.LENGTH_SHORT).show();
                break;

            case R.id.secondary_menu_filter:
                filterLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Filter Items", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }

    /*Add components to filter area*/
    private LinearLayout filterLayout;
    private Spinner mSortBySpinner;

    private RadioGroup mFilterRadioGroup;
    private RadioButton mRadioButtonhtl, mRadioButtonlth;
    private Button mHighRated, mLowRated;
    private void populateFilterView() {
        filterLayout = findViewById(R.id.top_filter_layout);
        mSortBySpinner = findViewById(R.id.sort_by);
        mHighRated = findViewById(R.id.filter_high_rated);
        mHighRated.setOnClickListener( this);
        mLowRated = findViewById(R.id.filter_low_rated);
        mLowRated.setOnClickListener(this);

        String[] stringArray = this.getResources().getStringArray(R.array.sort_by);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_items, stringArray);
        mSortBySpinner.setAdapter(stringArrayAdapter);
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recentRecyclerAdapter.notifyDataSetChanged();
                if (adapterView.getItemAtPosition(i).toString().equals("Newest First")){
                    Query query =  itemCollection.orderBy("itemNumber", Query.Direction.DESCENDING).whereEqualTo("category", intentString);
                  updateFirestoreRecyclerOption(query);
                }else if(adapterView.getItemAtPosition(i).toString().equals("Oldest First")){
                    Query query =  itemCollection.orderBy("itemNumber", Query.Direction.ASCENDING).whereEqualTo("category", intentString);
                    updateFirestoreRecyclerOption(query);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFilterRadioGroup = findViewById(R.id.filter_radio_group);
        mFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Query query;
                switch (i){
                    case R.id.filter_radio_button_htl:
                        query = itemCollection.orderBy("price", Query.Direction.DESCENDING).whereEqualTo("category", intentString);
                        updateFirestoreRecyclerOption(query);
                        break;
                    case R.id.filter_radio_button_lth:
                        query = itemCollection.orderBy("price", Query.Direction.ASCENDING).whereEqualTo("category", intentString);
                            updateFirestoreRecyclerOption(query);
                            break;
                }
            }
        });
    }


    /*Update Firestore recycler query Layout*/
    private void updateFirestoreRecyclerOption(Query query) {
        FirestoreRecyclerOptions<ItemModel> option = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class)
                .build();
        recentRecyclerAdapter.updateOptions(option);
    }

    /*Handle qurey*/
    private void handleQurey(Query query, FirestoreRecyclerOptions<ItemModel> options){
        recentRecyclerAdapter = new SelectedIItemRecyclerAdapter(options);
        FlexboxLayoutManager layoutManager1 =new FlexboxLayoutManager(this);
        layoutManager1.setJustifyContent ( JustifyContent.SPACE_EVENLY );
        layoutManager1.setFlexWrap ( FlexWrap.WRAP );
        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setAdapter(recentRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        recentRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recentRecyclerAdapter.stopListening();
    }

    @Override
    public void onClick(View view) {
        Query query;
        switch (view.getId()){
            case R.id.filter_high_rated:
                query = itemCollection.orderBy("rating", Query.Direction.DESCENDING).whereEqualTo("category", intentString);
                updateFirestoreRecyclerOption(query);
                break;
            case R.id.filter_low_rated:
                query = itemCollection.orderBy("rating", Query.Direction.ASCENDING).whereEqualTo("category", intentString);
                updateFirestoreRecyclerOption(query);
                break;
        }
    }
}