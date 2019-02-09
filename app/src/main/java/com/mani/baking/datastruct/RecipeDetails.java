package com.mani.baking.datastruct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetails implements Parcelable {

    private String id;
    private String name;
    private String servings;
    private String imageUrl;
    private List<IngredientDetails> ingredientDetailsList;
    private List<StepDetails> stepDetailsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<IngredientDetails> getIngredientDetailsList() {
        return ingredientDetailsList;
    }

    public void setIngredientDetailsList(List<IngredientDetails> ingredientDetailsList) {
        this.ingredientDetailsList = ingredientDetailsList;
    }

    public List<StepDetails> getStepDetailsList() {
        return stepDetailsList;
    }

    public void setStepDetailsList(List<StepDetails> stepDetailsList) {
        this.stepDetailsList = stepDetailsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.servings);
        dest.writeString(this.imageUrl);
        dest.writeList(this.ingredientDetailsList);
        dest.writeList(this.stepDetailsList);
    }

    public RecipeDetails() {

    }

    private RecipeDetails(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        servings = parcel.readString();
        imageUrl = parcel.readString();
        ingredientDetailsList = new ArrayList<>();
        stepDetailsList = new ArrayList<>();
        parcel.readList(ingredientDetailsList, IngredientDetails.class.getClassLoader());
        parcel.readList(stepDetailsList, RecipeDetails.class.getClassLoader());
    }

    public static final Creator<RecipeDetails> CREATOR = new Creator<RecipeDetails>() {
        @Override
        public RecipeDetails createFromParcel(Parcel parcel) {
            return new RecipeDetails(parcel);
        }

        @Override
        public RecipeDetails[] newArray(int size) {
            return new RecipeDetails[size];
        }
    };
}
