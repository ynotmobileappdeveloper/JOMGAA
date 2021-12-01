package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.CartData;

import java.util.List;

public class CartItems {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("discounts")
    @Expose
    private Integer discounts;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("minimum_purchase_amount")
    @Expose
    private String minimumPurchaseAmount;
    @SerializedName("cart_item")
    @Expose
    private List<CartData> cartItem = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Integer discounts) {
        this.discounts = discounts;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(String minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public List<CartData> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartData> cartItem) {
        this.cartItem = cartItem;
    }
}
