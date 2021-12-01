package com.ynot.jomgaa.Model;

public class CartModel {

    private String id;
    private String image;
    private String name;
    private String price;
    private String discount;
    private String weight;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    private  String stock;
    private String fav_status;
    private String cart_count;

    public boolean isAvilability() {
        return avilability;
    }

    public void setAvilability(boolean avilability) {
        this.avilability = avilability;
    }

    boolean avilability;





    public CartModel(String id, String image, String name, String price, String discount, String weight, String fav_status, String cart_count,String stock) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.weight = weight;
        this.fav_status = fav_status;
        this.cart_count = cart_count;
        this.stock=stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }

    public String getCart_count() {
        return cart_count;
    }

    public void setCart_count(String cart_count) {
        this.cart_count = cart_count;
    }
}
