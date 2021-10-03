package com.niteshb.mywardrobe.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.activities.AddItemActivity;
import com.niteshb.mywardrobe.interfaces.ItemClickListener;
import com.niteshb.mywardrobe.models.FilterModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class FilterDialogFragment extends DialogFragment {

    private ItemClickListener itemClickListener;
    private FilterModel filterModel;
    private ArrayList<String> subTypes;

    private Button mButton_asc, mButton_des;
    private CheckBox sizeXS, sizeS, sizeM, sizeL, sizeXL;
    private EditText mColorEditText, mKeywordEditText;
    private Spinner selectSubType;
    private Map<String, String> sizes;

    private Button cancelButton, okButton;

    public FilterDialogFragment(ItemClickListener itemClickListener, ArrayList<String> subTypes){
        this.itemClickListener = itemClickListener;
        this.subTypes = subTypes;
        subTypes.add(0, "Select type");
    }

    public FilterDialogFragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE,
                R.style.MyDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);

        mButton_asc = view.findViewById(R.id.button_asc);
        mButton_des = view.findViewById(R.id.button_des);
        sizeXS = view.findViewById(R.id.checkbox_xs);
        sizeS = view.findViewById(R.id.checkbox_s);
        sizeM = view.findViewById(R.id.checkbox_M);
        sizeL = view.findViewById(R.id.checkbox_L);
        sizeXL = view.findViewById(R.id.checkbox_xl);
        mColorEditText = view.findViewById(R.id.editText_color);
        mKeywordEditText = view.findViewById(R.id.editText_keyword);
        cancelButton = view.findViewById(R.id.button_cancel);
        okButton = view.findViewById(R.id.button_ok);
        selectSubType = view.findViewById(R.id.spinner_subtype);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterModel = new FilterModel();
        sizes = new HashMap<>();
        mButton_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterModel.setDisplayOrder(1);
                mButton_asc.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_button_selected));
                mButton_asc.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                mButton_des.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_button));
                mButton_des.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
            }
        });
        mButton_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterModel.setDisplayOrder(2);
                mButton_des.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_button_selected));
                mButton_des.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));

                mButton_asc.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_button));
                mButton_asc.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
            }
        });

        sizeXS.setOnCheckedChangeListener(checkChangeButton);
        sizeS.setOnCheckedChangeListener(checkChangeButton);
        sizeM.setOnCheckedChangeListener(checkChangeButton);
        sizeL.setOnCheckedChangeListener(checkChangeButton);
        sizeXL.setOnCheckedChangeListener(checkChangeButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterModel.setSize(new ArrayList<>(sizes.values()));
                filterModel.setColor(mColorEditText.getText().toString());
                filterModel.setKeyword(mKeywordEditText.getText().toString());
                itemClickListener.onItemClick(filterModel, 0);
                dismiss();
            }
        });



        if((subTypes != null) && (subTypes.size() > 0)){
            //  String[] subTypesArray = subTypes.toArray(new String[subTypes.size()]);
            ArrayAdapter<String> categoryArray = new ArrayAdapter<>(view.getContext(), R.layout.item_spinner_textview, R.id.textView, subTypes);
            selectSubType.setAdapter(categoryArray);
            (view.findViewById(R.id.subtype_holder)).setVisibility(View.VISIBLE);
        }else{
            (view.findViewById(R.id.subtype_holder)).setVisibility(View.GONE);
        }

        selectSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    filterModel.setSubType(adapterView.getSelectedItem().toString());
                }else{
                    filterModel.setSubType(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    CompoundButton.OnCheckedChangeListener checkChangeButton = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if(checked){
                sizes.put(compoundButton.getText().toString(), compoundButton.getText().toString());
            }else{
                sizes.remove(compoundButton.getText().toString());
            }
        }
    };



}