package com.mani.baking.datastruct;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientDetails implements Parcelable {
    private int quantity;
    private String measure;
    private String ingredient;
public IngredientDetails(){}
    protected IngredientDetails(Parcel in) {
        quantity = in.readInt();
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
