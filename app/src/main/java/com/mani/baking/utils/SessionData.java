package com.mani.baking.utils;

import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.datastruct.StepDetails;

import java.util.List;

public class SessionData {
    public static List<RecipeDetails> recipeDetailsList;

    public static RecipeDetails getRecipeDetails(int position) {
        return recipeDetailsList.get(position);
    }

    public static RecipeDetails getRecipeDetails() {
        return recipeDetailsList.get(SelectionSesionVar.recipe);
    }

    public static StepDetails getStepDetails(int stepPosition) {
        return getRecipeDetails(SelectionSesionVar.recipe).getStepDetailsList().get(stepPosition);
    }

    public static StepDetails getStepDetails() {
        return getRecipeDetails(SelectionSesionVar.recipe)
                .getStepDetailsList().get(SelectionSesionVar.step);
    }
}
