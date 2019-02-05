package com.mani.baking.utils;

import android.content.Context;
import android.util.Log;

import com.mani.baking.R;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.datastruct.StepDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExtractJson {
    private static final String TAG = ExtractJson.class.getSimpleName();


    /**
     * Parse the R.raw.baking converts the json to List<RecipeDetails>
     * On any error empty string or 0 or empty list is set as applicable
     * @param context context
     * @return List<RecipeDetails>
     */
    public List<RecipeDetails> getRecipeDetails(Context context) {
        JSONArray recipeDetailsJson = readJsonFromRaw(context);
        List<RecipeDetails> recipeDetailsList = new ArrayList<>();
        if (recipeDetailsJson.length() == 0) {
            return new ArrayList<>();
        }

        for (int i = 0; i < recipeDetailsJson.length(); i++) {
            RecipeDetails recipeDetails = new RecipeDetails();
            JSONObject recipeJson;
            try {
                 recipeJson = recipeDetailsJson.getJSONObject(i);
            }catch (Exception e){
                continue;
            }
            recipeDetails.setId(getIntFromJson(recipeJson, KeyConstants.id));
            recipeDetails.setName(getStringFromJson(recipeJson, KeyConstants.name));
            recipeDetails.setServings(getStringFromJson(recipeJson, KeyConstants.servings));
            recipeDetails.setImageUrl(getStringFromJson(recipeJson, KeyConstants.image));
            recipeDetails.setIngredientDetailsList(getIngredientsList(recipeJson));
            recipeDetails.setStepDetailsList(getStepList(recipeJson));
            recipeDetailsList.add(recipeDetails);
        }
        return recipeDetailsList;
    }

    private JSONArray readJsonFromRaw(Context context) {

        InputStreamReader inputStream;
        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = new InputStreamReader(context.getResources().openRawResource(R.raw.baking), StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStream);
            String string = reader.readLine();
            while (string != null) {
                stringBuilder.append(string);
                string = reader.readLine();
            }
            return new JSONArray(stringBuilder.toString());

        } catch (IOException | JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return new JSONArray();
    }

    private List<IngredientDetails> getIngredientsList(JSONObject jsonObject) {
        JSONArray ingredientsJsonArray;
        List<IngredientDetails> ingredientDetailsList = new ArrayList<>();
        try {
            ingredientsJsonArray = jsonObject.getJSONArray(KeyConstants.ingredients);

        } catch (Exception e) {
            return ingredientDetailsList;
        }

        for (int i = 0; i < ingredientsJsonArray.length(); i++) {
            IngredientDetails ingredientDetails = new IngredientDetails();
            JSONObject ingredientJson;
            try {
                ingredientJson = ingredientsJsonArray.getJSONObject(i);
            } catch (Exception e) {
                continue;
            }
            ingredientDetails.setIngredient(getStringFromJson(ingredientJson, KeyConstants.ingredient));
            ingredientDetails.setMeasure(getStringFromJson(ingredientJson, KeyConstants.measure));
            ingredientDetails.setQuantity(getIntFromJson(ingredientJson, KeyConstants.quantity));
            ingredientDetailsList.add(ingredientDetails);
        }
        return ingredientDetailsList;
    }

    private List<StepDetails> getStepList(JSONObject jsonObject) {
        JSONArray stepJsonArray;
        List<StepDetails> stepDetailsList = new ArrayList<>();
        try {
            stepJsonArray = jsonObject.getJSONArray(KeyConstants.steps);

        } catch (Exception e) {
            return stepDetailsList;
        }

        for (int i = 0; i < stepJsonArray.length(); i++) {
            StepDetails stepDetails = new StepDetails();
            JSONObject stepJson;
            try {
                stepJson = stepJsonArray.getJSONObject(i);
            } catch (Exception e) {
                continue;
            }
            stepDetails.setId(getIntFromJson(stepJson, KeyConstants.id));
            stepDetails.setShortDescribtion(getStringFromJson(stepJson, KeyConstants.shortDescription));
            stepDetails.setDescribtion(getStringFromJson(stepJson, KeyConstants.description));
            stepDetails.setVideoUrl(getStringFromJson(stepJson, KeyConstants.videoURL));
            stepDetails.setThumbnailUrl(getStringFromJson(stepJson, KeyConstants.thumbnailURL));
            stepDetailsList.add(stepDetails);
        }
        return stepDetailsList;
    }

    private int getIntFromJson(JSONObject jsonObject, String key) {
        int returnVal = 0;
        try {
            returnVal = jsonObject.optInt(key, returnVal);
        } catch (Exception e) {
            //handle - data type of id as string
            try {
                String string = jsonObject.optString(key, Integer.toString(returnVal));
                returnVal = Integer.parseInt(string);
            } catch (Exception e1) {
                Log.d(TAG, e.getMessage());
            }
        }
        return returnVal;
    }

    private String getStringFromJson(JSONObject jsonObject, String key) {
        String returnVal = "";
        try {
            returnVal = jsonObject.optString(key, returnVal);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return returnVal;
    }

}
