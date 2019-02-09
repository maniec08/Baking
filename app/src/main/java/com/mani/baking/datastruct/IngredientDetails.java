package com.mani.baking.datastruct;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientDetails implements Parcelable {
    private String quantity;
    private String measure;
    private String ingredient;
public IngredientDetails(){}
    private IngredientDetails(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<IngredientDetails> CREATOR = new Creator<IngredientDetails>() {
        @Override
        public IngredientDetails createFromParcel(Parcel in) {
            return new IngredientDetails(in);
        }

        @Override
        public IngredientDetails[] newArray(int size) {
            return new IngredientDetails[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
