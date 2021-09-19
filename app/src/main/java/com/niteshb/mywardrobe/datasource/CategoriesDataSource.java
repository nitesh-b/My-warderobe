package com.niteshb.mywardrobe.datasource;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.niteshb.mywardrobe.interfaces.SuccessListener;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubTypesModel;
import com.niteshb.mywardrobe.realmHelper.RealmHelper;

import java.util.ArrayList;

import io.realm.Realm;

public class CategoriesDataSource {

    public static void setAllCategories(SuccessListener listener){
                FirebaseFirestore.getInstance()
                .collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(DocumentSnapshot ds: task.getResult().getDocuments()){
                                CategoryModel model = ds.toObject(CategoryModel.class);
                                RealmHelper.insert(model);
                            }
                            setSubCategories(listener);
                        }else{
                            listener.onFailure("Could not get result");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e.getLocalizedMessage());
                    }
                });


    }

    private static void setSubCategories(SuccessListener listener) {
        FirebaseFirestore.getInstance().collection("sub-categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            for(DocumentSnapshot ds: task.getResult().getDocuments()){
                                SubTypesModel subTypesModel = ds.toObject(SubTypesModel.class);
                                SubCategoryModel model = new SubCategoryModel(subTypesModel);
                                RealmHelper.insert(model);
                            }
                            listener.onSuccess();
                        }else{
                            listener.onFailure("Could not get result");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getLocalizedMessage());
            }
        });
    }


}
