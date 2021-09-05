package com.niteshb.mywardrobe.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.repositories.ItemRepository;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseItemViewModel  extends ViewModel {

    private MutableLiveData<List<ItemModel>> mItemListMutableData;
    private ItemRepository itemRepository;
    public LiveData<List<ItemModel>> getItemLiveData(){
        return mItemListMutableData;
    }

    public void init(){
        if(mItemListMutableData != null){
            Log.d(TAG, "init: data :" +mItemListMutableData);
            return;
        }
        itemRepository = ItemRepository.getInstance();
        mItemListMutableData = itemRepository.getItemModel();
    }
}
