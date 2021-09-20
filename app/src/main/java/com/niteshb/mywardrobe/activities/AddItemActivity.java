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
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, FirebaseDataUpload.FirebaseDataUploadListener {
    private static final String TAG = "Add Item Tag";
    private static final int CAMERA_PIC_REQUEST = 100;
    private Spinner selectSubType;
    private EditText mAdditionalInformation;
    private Button saveBtn, cancelBtn;
    private String currentPhotoPath;
    private RadioGroup rGroup;
    private RadioButton b1, b2, b3, b4, b5;
    private FirebaseAuth mAuth;
    private LinearLayout subTypeHolder;
    private ImageView itemImage;
    private FirebaseDataUpload upload;
    private ArrayList<String> subTypes;
    private SubCategoryModel subCategoryModel;
    private String subCategoryId;
    private ItemModel itemModel;
    private String size;

    /*Firebase*/

    private Uri imageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        subCategoryId = getIntent().getStringExtra("SUB_CATEGORY_ID");
        selectSubType = findViewById(R.id.spinner_subTypes);
        subTypeHolder = findViewById(R.id.linearLayout_subType_holder);
        itemImage = findViewById(R.id.clicked_image);
        itemImage.setOnClickListener(this);
        /*Select Image First*/

        requestCameraPermission();
        subCategoryModel = RealmHelper.getSubCategory(subCategoryId);
        subTypes = RealmHelper.getSubTypes(subCategoryId);
        if(subCategoryModel != null){
            itemModel = new ItemModel(mAuth.getUid(), subCategoryModel.getCategoryId(), subCategoryModel.getId(), false);
            itemModel.setSubCategory(subCategoryModel.getSubCategory());
        }else{

            finish();
            return;
        }


        /*Select Category and Sub-category*/
        if(subTypes.size()>0){
            String[] subTypesArray = subTypes.toArray(new String[subTypes.size()]);
            ArrayAdapter<String> categoryArray = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_item, subTypesArray);
            selectSubType.setAdapter(categoryArray);
        }else{
            subTypeHolder.setVisibility(View.GONE);
        }

        selectSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AddItemActivity.this, "itemClicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemSelected: Selected Item: " + adapterView.getItemAtPosition(i));
                String subType = adapterView.getItemAtPosition(i).toString();
                itemModel.setSubType(subType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddItemActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

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
                    case (R.id.button_xs):
                        size = b1.getText().toString();
                        break;
                    case (R.id.button_s):
                        size = b2.getText().toString();
                        break;
                    case (R.id.button_m):
                        size = b3.getText().toString();
                        break;
                    case (R.id.button_l):
                        size = b4.getText().toString();
                        break;
                    case (R.id.button_xl):
                        size = b5.getText().toString();
                        break;
                }
                itemModel.setSize(size);

            }
        });


        /*Additional Information*/
        mAdditionalInformation = findViewById(R.id.additional_information);


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
            case (R.id.clicked_image):
                //open camera to take picture. Save image and get image reference
                dispatchTakePictureIntent();
                break;


            /*Progress Bar Show hide with translucent Scrollview*/
            case (R.id.save_button):
               if(mAdditionalInformation.getText().toString().isEmpty()){
                  itemModel.setDescription("");
               }else {
                   itemModel.setDescription(mAdditionalInformation.getText().toString());
               }
                itemModel.setCategory(RealmHelper.getCategory(subCategoryModel.getCategoryId()).getCategory());
                upload = new FirebaseDataUpload(this, itemModel, imageReference);
                upload.show(getSupportFragmentManager(), "upload");
                break;

            case (R.id.cancel_button):
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
            itemImage.setImageURI(imageReference);

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