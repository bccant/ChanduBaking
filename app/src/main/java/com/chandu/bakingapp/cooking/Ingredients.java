package com.chandu.bakingapp.cooking;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Ingredients implements Serializable {
    public Ingredients() {

    }

    @SerializedName("quantity")
    public String Quantity;

    @SerializedName("measure")
    public String Measure;

    @SerializedName("ingredient")
    public String Ingredient;

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }
}
