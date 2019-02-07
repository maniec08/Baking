package com.mani.baking.datastruct;

import android.content.Context;

import com.mani.baking.utils.ExtractJson;

import java.util.List;

public class Recipe {
    public static int selectedRecipe = 0;
    public static int selectedStep = -1;
    public static List<RecipeDetails> recipeDetailsList;
    private Context context;
    public Recipe(){}
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

    public static StepDetails getStepDetails(int recipePosition, int stepPosition){
        return getRecipeDetails(recipePosition).getStepDetailsList().get(stepPosition);
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

    public static void setDetailsList(List<RecipeDetails> recipeDetailsList) {
        Recipe.recipeDetailsList = recipeDetailsList;
    }
}
