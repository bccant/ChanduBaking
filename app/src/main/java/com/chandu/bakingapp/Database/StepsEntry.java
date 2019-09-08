package com.chandu.bakingapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "recipesteps")
public class StepsEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long ingID;
    private String stepsId;
    private String stepsShortDesc;
    private String stepsDesc;
    private String stepsVideoUrl = "";
    private String stepsThumbUrl = "";
    private int recipeId;

    public StepsEntry (@NonNull String stepsId, String stepsShortDesc, String stepsDesc, String stepsVideoUrl,
                       String stepsThumbUrl, int recipeId) {
        this.stepsId = stepsId;
        this.recipeId = recipeId;
        this.stepsDesc = stepsDesc;
        this.stepsShortDesc = stepsShortDesc;
        this.stepsThumbUrl = stepsThumbUrl;
        this.stepsVideoUrl = stepsVideoUrl;
    }

    @NonNull
    public String getStepsId() {
        return stepsId;
    }

    public void setStepsId(@NonNull String stepsId) {
        this.stepsId = stepsId;
    }

    public String getStepsShortDesc() {
        return stepsShortDesc;
    }

    public void setStepsShortDesc(String stepsShortDesc) {
        this.stepsShortDesc = stepsShortDesc;
    }

    public String getStepsDesc() {
        return stepsDesc;
    }

    public void setStepsDesc(String stepsDesc) {
        this.stepsDesc = stepsDesc;
    }

    public String getStepsVideoUrl() {
        return stepsVideoUrl;
    }

    public void setStepsVideoUrl(String stepsVideoUrl) {
        this.stepsVideoUrl = stepsVideoUrl;
    }

    public String getStepsThumbUrl() {
        return stepsThumbUrl;
    }

    public void setStepsThumbUrl(String stepsThumbUrl) {
        this.stepsThumbUrl = stepsThumbUrl;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public long getIngID() {
        return ingID;
    }

    public void setIngID(@NonNull long ingID) {
        this.ingID = ingID;
    }
}
