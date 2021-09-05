package com.niteshb.mywardrobe.repositories;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niteshb.mywardrobe.datasource.ItemDataSource;
import com.niteshb.mywardrobe.models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private static ItemRepository instance;
    private ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();

    /*Singleton pattern*/
    public static ItemRepository getInstance(){
        if(instance == null){
            instance = new ItemRepository();
        }
        return instance;
    }

    public MutableLiveData<List<ItemModel>> getItemModel(){
        setItemModel();//here we access the firebase data
        MutableLiveData<List<ItemModel>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(itemModelArrayList);
        return mutableLiveData;
    }

    private void setItemModel(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ItemDataSource dataSource = new ItemDataSource();
        itemModelArrayList = dataSource.getDataList(user.getEmail());

    }
}
