package com.mani.baking.datastruct;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mani.baking.utils.ExtractJson;
import com.mani.baking.utils.KeyConstants;

import java.util.List;

public class Recipe {
    /**
     * Session variables used to pass on the data
     */
    public static int selectedRecipe = 0;
    public static int selectedStep = -1;
    public static List<RecipeDetails> recipeDetailsList;

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

    public static RecipeDetails getRecipeDetails(int position){
       return recipeDetailsList.get(position);
    }
    public static RecipeDetails getRecipeDetails(){
        return recipeDetailsList.get(selectedRecipe);
    }

    public static StepDetails getStepDetails( int stepPosition){
        return getRecipeDetails(selectedRecipe).getStepDetailsList().get(stepPosition);
    }
    public static StepDetails getStepDetails( ){
        return getRecipeDetails(selectedRecipe).getStepDetailsList().get(selectedStep);
    }
    private void setRecipeDetailsList() {
        ExtractJson extractJson = new ExtractJson();
        Recipe.recipeDetailsList = extractJson.getRecipeDetails(context);
    }
}
