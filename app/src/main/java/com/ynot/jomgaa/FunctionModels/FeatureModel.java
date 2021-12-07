package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.FeatureList;

import java.util.List;

public class FeatureModel {
    private boolean status;

    @SerializedName("data")
    @Expose
    private List<FeatureList> list = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<FeatureList> getList() {
        return list;
    }

    public void setList(List<FeatureList> list) {
        this.list = list;
    }
}
