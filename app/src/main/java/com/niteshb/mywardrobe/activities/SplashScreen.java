package com.niteshb.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.datasource.CategoriesDataSource;
import com.niteshb.mywardrobe.interfaces.SuccessListener;
import com.niteshb.mywardrobe.utils.ProgressDialog;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashScreen extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressDialog = new ProgressDialog(this);
        initializeRealm();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            getCategories();
        }else{
            failedToInitialize("login first");
        }
    }


    private void initializeRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wardrobe.realm")
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(configuration);


    }

    private void getCategories(){
        progressDialog.show();
        CategoriesDataSource.setAllCategories(new SuccessListener() {
            @Override
            public void onSuccess() {
                startMainActivity();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                failedToInitialize(errorMessage);
                progressDialog.dismiss();
            }
        });
    }



    private void startMainActivity() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }



    private void failedToInitialize(String errorMessage) {
        finish();
        startActivity(new Intent(this, Login.class));
    }
}
