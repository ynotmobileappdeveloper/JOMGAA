package com.ynot.jomgaa.FunctionModels;

import android.hardware.lights.LightsManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductsModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("products")
    @Expose
    private List<Products> model = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Products> getModel() {
        return model;
    }

    public void setModel(List<Products> model) {
        this.model = model;
    }
}
