package com.niteshb.mywardrobe.realmHelper;

import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    public static void insert(RealmObject realmObject){
        if(realmObject != null){
            try(Realm realm = Realm.getDefaultInstance()){
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                            realm.insertOrUpdate(realmObject);
                    }
                });
            }
        }
    }

    public static ArrayList<CategoryModel> getCategories() {
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        try(Realm realm = Realm.getDefaultInstance()){
            RealmResults<CategoryModel> results =  realm.where(CategoryModel.class).findAll();
            if(results!= null && !results.isEmpty()){
                categoryModels.addAll(realm.copyFromRealm(results));
            }
        }
        return categoryModels;
    }

    public static CategoryModel getCategory(String categoryId) {
        CategoryModel model  = new CategoryModel();
        try(Realm realm = Realm.getDefaultInstance()){
            CategoryModel result =  realm.where(CategoryModel.class).equalTo("id", categoryId).findFirst();
            if(result!= null){
               model= realm.copyFromRealm(result);
            }
            return  model;
        }
    }

    public static ArrayList<SubCategoryModel> getSubCategories(String categoryId) {
        ArrayList<SubCategoryModel> subCategoryModels = new ArrayList<>();
        try(Realm realm = Realm.getDefaultInstance()){
            RealmResults<SubCategoryModel> result =  realm.where(SubCategoryModel.class).equalTo("categoryId", categoryId).findAll();
            if(result!= null){
                subCategoryModels.addAll(realm.copyFromRealm(result));
            }
            return  subCategoryModels;
        }
    }

    public static RealmResults<ItemModel> getItems(String userId, String categoryId) {
        try(Realm realm = Realm.getDefaultInstance()){
            return realm.where(ItemModel.class)
                    .equalTo("userId", userId)
                    .and()
                    .equalTo("categoryId", categoryId)
                    .findAll();

        }
    }
}
