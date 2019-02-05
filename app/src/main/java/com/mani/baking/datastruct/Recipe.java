package com.mani.baking.datastruct;

import android.content.Context;

import com.mani.baking.utils.ExtractJson;

import java.util.List;

public class Recipe {

    private static List<RecipeDetails> recipeDetailsList;
    private Context context;

    public Recipe(Context context) {
        this.context = context;
    }

    public List<RecipeDetails> getList() {
        if (recipeDetailsList == null || recipeDetailsList.isEmpty()) {
            setRecipeDetailsList();
        }
        return recipeDetailsList;
    }

    private void setRecipeDetailsList() {
        ExtractJson extractJson = new ExtractJson();
        Recipe.recipeDetailsList = extractJson.getRecipeDetails(context);
    }

    public static void setDetailsList(List<RecipeDetails> recipeDetailsList) {
        Recipe.recipeDetailsList = recipeDetailsList;
    }
}
