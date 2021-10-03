package com.niteshb.mywardrobe.models;

import java.util.ArrayList;

public class FilterModel {
    private ArrayList<String> size;
    private int displayOrder; //ASC == 1 , DES == 2
    private String color;
    private String keyword;
    private String subType;

    public FilterModel() {
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public ArrayList<String>  getSize() {
        return size;

    }

    public void setSize(ArrayList<String>  size) {
        this.size = size;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
