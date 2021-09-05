package com.niteshb.mywardrobe.interfaces;

import com.niteshb.mywardrobe.models.ItemModel;

import java.util.ArrayList;

public interface FirebaseDataLoad {
   void onFireBaseLoadSucccess(ArrayList<ItemModel> itemModelArrayList);
}
