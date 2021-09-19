package com.niteshb.mywardrobe.models.realmModels;

import java.util.ArrayList;

public class SubTypesModel {

    private String id;

    private String categoryId;

    private String subCategory;

    private ArrayList<String> subTypes;


    public SubTypesModel() {
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

    public ArrayList<String> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(ArrayList<String> subTypes) {
        this.subTypes = subTypes;
    }
}
