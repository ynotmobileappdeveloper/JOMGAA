package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.OrderData;

import java.util.List;

public class OrderItems {
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<OrderData> orderData = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<OrderData> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderData> orderData) {
        this.orderData = orderData;
    }
}
