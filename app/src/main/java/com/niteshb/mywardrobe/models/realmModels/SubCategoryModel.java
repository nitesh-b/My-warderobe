package com.niteshb.mywardrobe.models.realmModels;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SubCategoryModel extends RealmObject {

    @PrimaryKey
    private String id;

    private String categoryId;

    private String subCategory;

    private RealmList<String> subTypes;


    public SubCategoryModel() {
    }

    public SubCategoryModel(SubTypesModel subTypesModel) {
        this.id = subTypesModel.getId();
        this.categoryId = subTypesModel.getCategoryId();
        this.subCategory = subTypesModel.getSubCategory();
        this.subTypes = new RealmList<>();
        subTypes.addAll(subTypesModel.getSubTypes());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public RealmList<String> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(RealmList<String> subTypes) {
        this.subTypes = subTypes;
    }
}
