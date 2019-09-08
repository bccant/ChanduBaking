package com.chandu.bakingapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "recipe")
public class RecipeEntry {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String recipeId;
    private String recipeName;
    private String recipeServings;
    private String recipeImageUrl;

    public RecipeEntry(@NonNull String recipeId, String recipeName, String recipeServings, String recipeImageUrl) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeServings = recipeServings;
        this.recipeImageUrl = recipeImageUrl;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(String recipeServings) {
        this.recipeServings = recipeServings;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }
}
