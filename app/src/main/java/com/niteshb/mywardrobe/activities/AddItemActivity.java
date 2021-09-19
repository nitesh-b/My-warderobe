package com.niteshb.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.dataupload.FirebaseDataUpload;
import com.niteshb.mywardrobe.models.ItemModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, FirebaseDataUpload.FirebaseDataUploadListener {
    private static final String TAG = "Add Item Tag";
    private static final int CAMERA_PIC_REQUEST = 100;
    private ImageButton selectImage;
    private Spinner selectCategory, selectType, selectColor;
    private EditText mPrice, mAdditionalInformation;
    private SeekBar mSeek;
    private AutoCompleteTextView mBrandName, mStoreName;
    private RatingBar mRatingBar;
    private Button saveBtn, cancelBtn;
    private String currentPhotoPath;
    private RadioGroup rGroup;
    private RadioButton b1, b2, b3, b4, b5;
    private FirebaseAuth mAuth;
    private LinearLayout mScrollView;
    private ImageView backgroundImage;
    private FirebaseDataUpload upload;

    /*Firebase*/

    private String category, type, color, price, brand, size, store, additionalInformation, rating;
    private Uri imageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        selectCategory = findViewById(R.id.spinner_category);
        selectType = findViewById(R.id.spinner_type);
        selectColor = findViewById(R.id.spinner_color);
        mPrice = findViewById(R.id.edittext_price);
        mSeek = findViewById(R.id.seekbar_price);
        mScrollView = findViewById(R.id.add_item_linear_layout);
        backgroundImage = findViewById(R.id.clicked_image);
        /*Select Image First*/

        requestCameraPermission();
        selectImage = findViewById(R.id.imageButton);
        selectImage.setVisibility(View.VISIBLE);
        selectImage.setOnClickListener(this);

        /*Select Category and Sub-category*/
        String[] categories = this.getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryArray = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, categories);
        selectCategory.setAdapter(categoryArray);
        selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AddItemActivity.this, "itemClicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemSelected: Selected Item: " + adapterView.getItemAtPosition(i));
                category = adapterView.getItemAtPosition(i).toString();
                String[] typeSelection = getResources().getStringArray(getStringResourceById(category));
                ArrayAdapter<String> typeArray = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, typeSelection);
                selectType.setAdapter(typeArray);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddItemActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });


        /*Select the color of the item*/
        String[] itemColor = getResources().getStringArray(R.array.color);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemColor);
        selectColor.setAdapter(colorAdapter);


        /*Set Price in SeekBar*/
        price = mPrice.getText().toString();
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPrice.setText(String.format("%d", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mSeek.setProgress(Integer.parseInt(mPrice.getText().toString()));


            }
        });


        /*AutoComplete Brand Name*/
        mBrandName = findViewById(R.id.autoCompleteBrand_brand);

        /*Size selection*/
        rGroup = findViewById(R.id.radio_group);
        b1 = findViewById(R.id.button_xs);
        b2 = findViewById(R.id.button_s);
        b3 = findViewById(R.id.button_m);
        b4 = findViewById(R.id.button_l);
        b5 = findViewById(R.id.button_xl);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.button_xs:
                        size = b1.getText().toString();
                        break;
                    case R.id.button_s:
                        size = b2.getText().toString();
                        break;
                    case R.id.button_m:
                        size = b3.getText().toString();
                        break;
                    case R.id.button_l:
                        size = b4.getText().toString();
                        break;
                    case R.id.button_xl:
                        size = b5.getText().toString();
                        break;
                }
            }
        });


        /*TODO AutoComplete Store Name*/
        mStoreName = findViewById(R.id.autoCompleteBrand_store);
        String[] storeArray = getResources().getStringArray(R.array.store_names);
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, storeArray);
        mStoreName.setAdapter(storeAdapter);

        /*Additional Information*/
        mAdditionalInformation = findViewById(R.id.additional_information);

        /*Rating Bar*/
        mRatingBar = findViewById(R.id.item_rating);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(mRatingBar.getRating());
            }
        });


        /*Save Button pressed*/
        saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);

    }

    private void requestCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*For populating spinner automatically*/
    private int getStringResourceById(String aString) {
        String packageName = getPackageName();
        return getResources().getIdentifier(aString, "array", packageName);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                //open camera to take picture. Save image and get image reference
                dispatchTakePictureIntent();
                selectImage.setVisibility(View.GONE);
                break;


            /*Progress Bar Show hide with translucent Scrollview*/
            case R.id.save_button:
                additionalInformation = mAdditionalInformation.getText().toString();
                rating = String.valueOf(mRatingBar.getRating());
                ItemModel model = new ItemModel();
                model.setCategory(category);
                upload = new FirebaseDataUpload(this, model, imageReference);
                upload.show(getSupportFragmentManager(), "upload");
                break;

            case R.id.cancel_button:
                finish();
                break;

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.niteshb.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "JPEG_" + "abc" + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */

        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            File file = new File(currentPhotoPath);
            imageReference = Uri.fromFile(file);
            Log.d(TAG, "onActivityResult: URi: " + imageReference);
            backgroundImage.setImageURI(imageReference);

        }
    }

    @Override
    public void uploadSuccess(Boolean complete) {
        if(complete){
            upload.dismiss();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}