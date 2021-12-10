package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.CurrentSize;
import com.ynot.jomgaa.Model.ImageModel;

import java.util.List;

public class productDetailModel {
    private boolean status;
    private String name;
    private String cart_quantity;
    private String details;
    private String color;
    private String original_price;
    private String price;
    @SerializedName("size")
    @Expose
    private List<SizeSpinner> size_model = null;

    @SerializedName("current_size")
    @Expose
    private List<CurrentSize> currentSizes = null;

    @SerializedName("image")
    @Expose
    private List<ImageModel> images = null;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public List<SizeSpinner> getSize_model() {
        return size_model;
    }

    public void setSize_model(List<SizeSpinner> size_model) {
        this.size_model = size_model;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<CurrentSize> getCurrentSizes() {
        return currentSizes;
    }

    public void setCurrentSizes(List<CurrentSize> currentSizes) {
        this.currentSizes = currentSizes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }
}
