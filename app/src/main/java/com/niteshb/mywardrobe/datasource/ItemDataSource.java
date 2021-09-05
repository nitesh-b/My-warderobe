package com.niteshb.mywardrobe.datasource;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.niteshb.mywardrobe.models.ItemModel;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ItemDataSource {
    private ArrayList<ItemModel> dataList = new ArrayList<>();
    private ItemModel model;
    public ArrayList<ItemModel> getDataList(String referencePath) {
        DocumentReference dRef = FirebaseFirestore.getInstance().collection("My-Wardrobe").document("Users");
        dRef.collection(referencePath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot qds: task.getResult()){
                        model = qds.toObject(ItemModel.class);
                        dataList.add(model);
                    }
                } else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return dataList;
    }

}
