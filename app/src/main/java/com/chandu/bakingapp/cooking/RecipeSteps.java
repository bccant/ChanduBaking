package com.chandu.bakingapp.cooking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeSteps implements Serializable {
    public RecipeSteps() {
    }

    @SerializedName("id")
    public String Id;

    @SerializedName("shortDescription")
    public String ShortDesc;

    @SerializedName("description")
    public String Desc;

    @SerializedName("videoURL")
    public String VideoUrl = "";

    @SerializedName("thumbnailURL")
    public String ThumbUrl = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getThumbUrl() {
        return ThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }
}
