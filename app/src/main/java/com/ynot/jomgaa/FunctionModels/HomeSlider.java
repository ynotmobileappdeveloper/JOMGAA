package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.Images;

import java.util.List;

public class HomeSlider {
    private String banner;
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<Images> data = null;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Images> getData() {
        return data;
    }

    public void setData(List<Images> data) {
        this.data = data;
    }
}
