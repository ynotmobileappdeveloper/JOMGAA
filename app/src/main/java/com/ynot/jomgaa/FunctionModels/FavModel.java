package com.ynot.jomgaa.FunctionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynot.jomgaa.Model.Favourite;

import java.util.List;

public class FavModel {
    @SerializedName("favourite")
    @Expose
    private List<Favourite> favourite = null;
    private boolean status = false;

    public List<Favourite> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<Favourite> favourite) {
        this.favourite = favourite;
    }

    public boolean getstatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
