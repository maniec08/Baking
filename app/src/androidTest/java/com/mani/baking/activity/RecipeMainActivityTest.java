package com.mani.baking.activity;


import android.content.res.Configuration;

import com.mani.baking.R;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.ExtractJson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.mani.baking.activity.utils.Assertion.getIngredientHeaderAssertion;
import static com.mani.baking.activity.utils.Assertion.getRowAssertion;
import static com.mani.baking.activity.utils.Assertion.getStepsHeaderAssertion;
import static com.mani.baking.activity.utils.Assertion.hasText;
import static com.mani.baking.activity.utils.Assertion.isDisabled;
import static com.mani.baking.activity.utils.Assertion.isViewEnabled;
import static com.mani.baking.activity.utils.Objects.getIngredientRecyclerView;
import static com.mani.baking.activity.utils.Objects.getIngredientsHeader;
import static com.mani.baking.activity.utils.Objects.getNextButton;
import static com.mani.baking.activity.utils.Objects.getPrevButton;
import static com.mani.baking.activity.utils.Objects.getRecipeObject;
import static com.mani.baking.activity.utils.Objects.getRecipeRecyclerView;
import static com.mani.baking.activity.utils.Objects.getStepDescription;
import static com.mani.baking.activity.utils.Objects.getStepsHeader;
import static com.mani.baking.activity.utils.Objects.getStepsRecyclerView;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeMainActivityTest {

    private List<RecipeDetails> recipeDetailsList;
    @Rule
    public ActivityTestRule<RecipeMainActivity> mActivityTestRule = new ActivityTestRule<>(RecipeMainActivity.class);

    @Before
    public void setUpSessionVar() {
        ExtractJson extractJson = new ExtractJson(InstrumentationRegistry.getInstrumentation().getTargetContext());
        extractJson.initializeSessionVar();
        recipeDetailsList = Recipe.recipeDetailsList;
    }

    /**
     * verify RecipeMainActivity activity displays all the recipes
     */
    @Test
    public void mainActivityTest() {
        for (int i = 0; i < recipeDetailsList.size(); i++) {
            getRecipeRecyclerView().perform(RecyclerViewActions.actionOnItemAtPosition(i,scrollTo()));
            getRecipeObject(i).check(hasText(recipeDetailsList.get(i).getName()));
        }
    }

    /**
     * Verify the step details contents are displayed for all recipes
     */
    @Test
    public void recipeStepDetailsTest() {
        for (int i = 0; i < recipeDetailsList.size(); i++) {
            getRecipeRecyclerView().perform(RecyclerViewActions.actionOnItemAtPosition(i, scrollTo()));
            getRecipeObject(i).perform(click());
            getStepsHeader().check(getStepsHeaderAssertion());
            List<StepDetails> stepDetailsList = recipeDetailsList.get(i).getStepDetailsList();
            for (int j = 0; j < stepDetailsList.size(); j++) {

                String stepVal = stepDetailsList.get(j).getShortDescription();
                onView(withId(R.id.steps_recycler_view))
                        .perform(RecyclerViewActions.scrollToPosition(j))
                        .check(getRowAssertion(stepVal));
            }
            Espresso.pressBack();
        }
    }

    /**
     * Verify the ingredients are displayed for one of the recipe
     * Navigate to Ingredient details and verify the content
     * Navigate to steps and make sure ingredients list is not displayed
     * Navigate to ingredients and verify step details is not displayed
     */
    @Test
    public void recipeIngredientsListTest() {
        boolean twoPane = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getBoolean(R.bool.istablet);
        int i = 0;
        getRecipeRecyclerView().perform(RecyclerViewActions.actionOnItemAtPosition(i, scrollTo()));
        getRecipeObject(i).perform(click());
        getIngredientsHeader().check(getIngredientHeaderAssertion());
        getIngredientsHeader().perform(click());
        List<IngredientDetails> ingredientDetailsList = recipeDetailsList.get(i).getIngredientDetailsList();
        for (int j = 0; j < ingredientDetailsList.size(); j++) {
            String quantity = ingredientDetailsList.get(j).getQuantity();
            getIngredientRecyclerView()
                    .perform(RecyclerViewActions.scrollToPosition(j))
                    .check(getRowAssertion(quantity));
        }
        if (!twoPane) {
            Espresso.pressBack();
            getIngredientRecyclerView().check(doesNotExist());
        }
        getStepsRecyclerView().check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        if (twoPane) {
            getStepsRecyclerView().check(matches(isDisplayed()));
        } else {
            getStepsRecyclerView().check(doesNotExist());
        }
        getIngredientRecyclerView().check(doesNotExist());

        if (!twoPane) {
            Espresso.pressBack();
        }
        getIngredientsHeader().check(getIngredientHeaderAssertion());
        getIngredientsHeader().perform(click());
        getIngredientRecyclerView().check(matches(isDisplayed()));
    }

    /**
     * verify the step navigation with steps detail fragment
     * first taps on second element and navigate to first element to ensure previous button is disabled
     * Then navigate until the last Step and verifies the next button is disabled on reaching last step
     * For tablet, both buttons should not be displayed
     */
    @Test
    public void stepNavigationTest() {
        boolean twoPane = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getBoolean(R.bool.istablet);
        Configuration configuration = InstrumentationRegistry.getInstrumentation().getContext().getResources().getConfiguration();
        boolean isLandscape = configuration.orientation == ORIENTATION_LANDSCAPE;
        int i = 0;

        getRecipeRecyclerView().perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
        getStepsRecyclerView().check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        if (!twoPane) {
            getPrevButton().perform(click());
            getPrevButton().check(isDisabled());
        }
        List<StepDetails> stepDetailsList = Recipe.getRecipeDetails(i).getStepDetailsList();

        for (int j = 0; j < stepDetailsList.size(); j++) {

            if (!isLandscape && !twoPane) {
                verifyPhonePortraitNavigation(stepDetailsList, j);
            } else if (isLandscape && !twoPane) {
                verifyPhoneLandscapeNavigation(stepDetailsList, j);
            } else {
                verifyTabletNavigation(stepDetailsList, j);
            }

        }

    }

    private void verifyTabletNavigation(List<StepDetails> stepDetailsList, int j) {
        getStepsRecyclerView().check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(j, click()));
        getPrevButton().check(matches(not(isDisplayed())));
        getNextButton().check(matches(not(isDisplayed())));
        getStepDescription().check(matches(withText(stepDetailsList.get(j).getDescription())));
    }

    private void verifyPhonePortraitNavigation(List<StepDetails> stepDetails, int j) {
        Espresso.pressBack();
        getStepsRecyclerView().check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(j, click()));
        getPrevButton().check(matches(isDisplayed()));
        getNextButton().check(matches(isDisplayed()));
        if (j == 0) {
            getPrevButton().check(isDisabled());
            getNextButton().check(isViewEnabled());
        }
        if (j == stepDetails.size()) {
            getPrevButton().check(isViewEnabled());
            getNextButton().check(isDisabled());
        }
        getStepDescription().check(matches(withText(stepDetails.get(j).getDescription())));
    }

    private void verifyPhoneLandscapeNavigation(List<StepDetails> stepDetails, int j) {
        Espresso.pressBack();
        getStepsRecyclerView().check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(j, click()));
        if (getVideoUrl(stepDetails.get(j)).isEmpty()) {
            getStepDescription().check(matches(withText(stepDetails.get(j).getDescription())));
            getPrevButton().check(matches(isDisplayed()));
            getNextButton().check(matches(isDisplayed()));
            if (j == 0) {
                getPrevButton().check(isDisabled());
                getNextButton().check(isViewEnabled());
            }
            if (j == stepDetails.size()) {
                getPrevButton().check(isViewEnabled());
                getNextButton().check(isDisabled());
            }
        } else {
            getPrevButton().check(matches(not(isDisplayed())));
            getNextButton().check(matches(not(isDisplayed())));
        }
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private String getVideoUrl(StepDetails stepDetails) {
        if (isNotNullOrEmpty(stepDetails.getVideoUrl())) {
            return stepDetails.getVideoUrl();
        }
        if (isNotNullOrEmpty(stepDetails.getThumbnailUrl())) {
            return stepDetails.getThumbnailUrl();
        }
        return "";
    }
}
