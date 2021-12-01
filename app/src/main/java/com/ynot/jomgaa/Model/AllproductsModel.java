package com.ynot.jomgaa.Model;

public class AllproductsModel {
    int img;

    public AllproductsModel(int img, String brand, String category, String price) {
        this.img = img;
        this.brand = brand;
        this.category = category;
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String brand,category,price;
}
