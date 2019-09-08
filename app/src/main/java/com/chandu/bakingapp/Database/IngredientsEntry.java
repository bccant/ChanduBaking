package com.chandu.bakingapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ingredients")
public class IngredientsEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long ingID;
    private String ingQuantity;
    private String ingMeasure;
    private String recipeIngredient;
    private int recipeId;

    public IngredientsEntry(String ingQuantity, String ingMeasure, String recipeIngredient, int recipeId) {
        this.ingQuantity = ingQuantity;
        this.ingMeasure = ingMeasure;
        this.recipeIngredient = recipeIngredient;
        this.recipeId = recipeId;
    }

    @NonNull
    public long getIngID() {
        return ingID;
    }

    public void setIngID(@NonNull long ingID) {
        this.ingID = ingID;
    }

    public String getIngQuantity() {
        return ingQuantity;
    }

    public void setIngQuantity(String ingQuantity) {
        this.ingQuantity = ingQuantity;
    }

    public String getIngMeasure() {
        return ingMeasure;
    }

    public void setIngMeasure(String ingMeasure) {
        this.ingMeasure = ingMeasure;
    }

    public String getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(String recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
