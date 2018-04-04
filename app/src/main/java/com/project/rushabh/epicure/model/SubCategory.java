package com.project.rushabh.epicure.model;

import java.io.Serializable;

/**
 * Created by brkckr on 28.10.2017.
 */

public class SubCategory implements Serializable {
    public String firebaseId;
    public int id;
    public String categoryId;
    public String name;
    public boolean isSelected = false;
    public boolean isExpanded = true;

    public SubCategory(String firebaseId, int id, String categoryId, String name) {
        this.firebaseId = firebaseId;
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public SubCategory(SubCategory subCategory) {
        this.firebaseId = subCategory.firebaseId;
        this.id = subCategory.id;
        this.categoryId = subCategory.categoryId;
        this.name = subCategory.name;
        this.isSelected = subCategory.isSelected;
    }

    public void setIsExpanded() {
        isExpanded = (!isExpanded);
    }
}
