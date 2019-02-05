package com.mani.baking.datastruct;

import android.os.Parcel;
import android.os.Parcelable;

public class StepDetails implements Parcelable {

    private int id;
    private String shortDescribtion;
    private String describtion;
    private String videoUrl;
    private String thumbnailUrl;

    public StepDetails() {
    }

    protected StepDetails(Parcel in) {
        id = in.readInt();
        shortDescribtion = in.readString();
        describtion = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescribtion() {
        return shortDescribtion;
    }

    public void setShortDescribtion(String shortDescribtion) {
        this.shortDescribtion = shortDescribtion;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
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
        dest.writeInt(id);
        dest.writeString(shortDescribtion);
        dest.writeString(describtion);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
