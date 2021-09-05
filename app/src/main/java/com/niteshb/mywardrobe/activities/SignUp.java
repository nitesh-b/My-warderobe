package com.niteshb.mywardrobe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.niteshb.mywardrobe.R;

import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {
 private EditText mFname, mLname, mEmail, mPassword,mConfirmPassword;
 private TextView mDob;
 private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFname = findViewById(R.id.signup_fname);
        mLname = findViewById(R.id.signup_lname);
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mConfirmPassword = findViewById(R.id.signup_confirm_password);
        mDob = findViewById(R.id.signup_dob);
        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUp.this,
                        android.R.style.Theme_Material_Light_Dialog,
                        dateSetListener,
                        day,month,year
                        );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                mDob.setText(date );
            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
