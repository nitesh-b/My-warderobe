package com.niteshb.mywardrobe.realmHelper;

import com.niteshb.mywardrobe.models.FilterModel;
import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.models.realmModels.CategoryModel;
import com.niteshb.mywardrobe.models.realmModels.SubCategoryModel;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

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

    public static SubCategoryModel getSubCategory(String subCategoryId) {
        try(Realm realm = Realm.getDefaultInstance()){
            SubCategoryModel result =  realm.where(SubCategoryModel.class).equalTo("id", subCategoryId).findFirst();
            if(result!= null){
                return realm.copyFromRealm(result);
            }
            return  null;
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

    //Get sub types from sub category id

    public static ArrayList<String> getSubTypes(String id) {
        ArrayList<String> categoryModels = new ArrayList<>();
        try(Realm realm = Realm.getDefaultInstance()){
            SubCategoryModel results =  realm.where(SubCategoryModel.class).equalTo("id", id).findFirst();
            if(results!= null){
                categoryModels.addAll(results.getSubTypes());
            }
        }
        return categoryModels;
    }


    public  static RealmResults<ItemModel> getCategoryItem(String categoryId, boolean favourite){
        try(Realm realm = Realm.getDefaultInstance()){
            if(favourite){
                return  realm.where(ItemModel.class).equalTo("categoryId", categoryId).and().equalTo("isFavourite", true).findAll();
            }else{
                return  realm.where(ItemModel.class).equalTo("categoryId", categoryId).findAll();
            }

        }
    }

    public static RealmResults<ItemModel> getSubCategoryItem(String subCategoryId, boolean favourite) {
        try(Realm realm = Realm.getDefaultInstance()){
            if(favourite){
                return  realm.where(ItemModel.class).equalTo("subCategoryId", subCategoryId).and().equalTo("isFavourite", true).findAll();
            }else{
                return  realm.where(ItemModel.class).equalTo("subCategoryId", subCategoryId).findAll();

            }
                   }
    }

    public static ItemModel getItem(String itemId) {
        try(Realm realm = Realm.getDefaultInstance()){
            return  realm.where(ItemModel.class).equalTo("id", itemId).findFirst();
        }
    }

    public static RealmResults<ItemModel> getFilteredItems(String categoryId, String subCategoryId, FilterModel filterModel) {
        try(Realm realm = Realm.getDefaultInstance()){
            RealmQuery<ItemModel> results =  realm.where(ItemModel.class);
            if(subCategoryId == null){
                results.equalTo("categoryId", categoryId);
            }else{
                results.equalTo("subCategoryId", subCategoryId);
                if(filterModel.getSubType() != null && !filterModel.getSubType().isEmpty()){
                    results.contains("subType", filterModel.getSubType(), Case.INSENSITIVE);
                }
            }

            if(filterModel.getDisplayOrder() == 1){
                results.sort("dateAdded", Sort.ASCENDING);
            }else if (filterModel.getDisplayOrder() == 2){
                results.sort("dateAdded", Sort.DESCENDING);
            }

            if(filterModel.getSize().size() > 0){
                results.in("size", filterModel.getSize().toArray(new String[filterModel.getSize().size()]));
            }

            if(!(filterModel.getColor() != null && filterModel.getColor().isEmpty())){
                results.contains("color", filterModel.getColor(), Case.INSENSITIVE);
            }
            if(!(filterModel.getKeyword() != null && filterModel.getKeyword().isEmpty())){
                results.contains("description", filterModel.getKeyword(), Case.INSENSITIVE);
            }

        return  results.findAll();
        }
    }
}
