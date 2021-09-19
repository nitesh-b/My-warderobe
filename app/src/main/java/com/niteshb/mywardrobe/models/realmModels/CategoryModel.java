package com.niteshb.mywardrobe.models.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CategoryModel extends RealmObject {
    @PrimaryKey
    private String id;

    private String category;

    public CategoryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
