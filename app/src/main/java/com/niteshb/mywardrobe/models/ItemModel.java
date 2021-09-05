package com.niteshb.mywardrobe.models;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String imageReference;
    private String  category, type, color, brand, size, store, additionalInformation;
    private int price;
    private float rating;
    private long itemNumber;

    public ItemModel() {
    }

    public ItemModel(String additionalInformation, String brand, String category, String color, String imageReference, long itemNumber, int price, float rating, String size, String store, String type){
        this.imageReference = imageReference;
        this.category = category;
        this.type = type;
        this.color = color;
        this.brand = brand;
        this.size = size;
        this.store = store;
        this.additionalInformation = additionalInformation;
        this.price = price;
        this.rating = rating;
        this.itemNumber = itemNumber;
    }

    public long getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getImageReference() {
        return imageReference;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
