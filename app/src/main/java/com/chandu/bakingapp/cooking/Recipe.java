package com.chandu.bakingapp.cooking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recipe implements Serializable {
    public Recipe() {
    }

    @SerializedName("id")
    public String Id;

    @SerializedName("name")
    public String Name;

    @SerializedName("ingredients")
    public Ingredients[] RecipeIngredients;

    @SerializedName("steps")
    public RecipeSteps[] RecipeSeq;

    @SerializedName("servings")
    public String Servings;

    @SerializedName("image")
    public String ImageUrl = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Ingredients[] getRecipeIngredients() {
        return RecipeIngredients;
    }

    public void setRecipeIngredients(Ingredients[] recipeIngredients) {
        RecipeIngredients = recipeIngredients;
    }

    public RecipeSteps[] getRecipeSeq() {
        return RecipeSeq;
    }

    public void setRecipeSeq(RecipeSteps[] recipeSeq) {
        RecipeSeq = recipeSeq;
    }

    public String getServings() {
        return Servings;
    }

    public void setServings(String servings) {
        Servings = servings;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
