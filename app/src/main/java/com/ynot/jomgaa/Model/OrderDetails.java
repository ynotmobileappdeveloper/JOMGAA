package com.ynot.jomgaa.Model;

public class OrderDetails {
    private  String id;
    private  String name;
    private  String qty;
    private  String amount;
    private  String image;

    public OrderDetails(String id, String name, String qty, String amount, String image) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.amount = amount;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
