package com.mani.baking.datastruct;

import android.os.Parcel;
import android.os.Parcelable;

public class StepDetails implements Parcelable {

    private String id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    public StepDetails() {
    }

    protected StepDetails(Parcel in) {
        id = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<StepDetails> CREATOR = new Creator<StepDetails>() {
        @Override
        public StepDetails createFromParcel(Parcel in) {
            return new StepDetails(in);
        }

        @Override
        public StepDetails[] newArray(int size) {
            return new StepDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
