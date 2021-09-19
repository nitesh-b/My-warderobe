package com.niteshb.mywardrobe.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.niteshb.mywardrobe.R;

public class ProgressDialog extends Dialog {
    private final TextView textViewMessage;
    private final TextView textViewTitle;
    public ProgressDialog(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_dialog, null);
        textViewMessage = view.findViewById(R.id.textView_message);
        textViewTitle  = view.findViewById(R.id.textView_title);
        setContentView(view);
    }

    public void setMessage(String message){
        textViewMessage.setText(message);
    }


    public void setHeading(String title){
        textViewTitle.setText(title);
    }

}
