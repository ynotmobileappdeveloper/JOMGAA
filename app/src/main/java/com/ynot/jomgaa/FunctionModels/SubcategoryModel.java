package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.Subcategory;

import java.util.List;

public class SubcategoryModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("subcategory")
    @Expose
    private List<Subcategory> subcategory = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

}