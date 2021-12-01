package com.ynot.jomgaa.Model;

import java.io.Serializable;

public class Subcategory implements Serializable {

    private String subcat_id = "";
    private String images = "";
    private String title = "";


    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
