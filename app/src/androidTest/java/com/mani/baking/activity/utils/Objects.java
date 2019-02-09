package com.mani.baking.activity.utils;

import com.mani.baking.R;

import org.hamcrest.Matchers;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.mani.baking.activity.utils.CustomMatcher.withIndex;
import static org.hamcrest.core.AllOf.allOf;

public class Objects {

    public static ViewInteraction getRecipeObject(int position){
        return onView(allOf(isDescendantOfA(withId(R.id.recipe_recycler_view)),
                withIndex(withId(R.id.recipe_text_view),position)));
    }

    public static ViewInteraction getStepsObject(int position){
        return onView(allOf(isDescendantOfA(withId(R.id.steps_recycler_view)),
                withIndex(withId(R.id.steps_content_tv),position)));
    }

    public static ViewInteraction getIngredientsHeader(){
        return onView(Matchers.allOf(hasSibling(withId(R.id.steps_recycler_view)), withText(R.string.ingredients_header)));
    }
    public static ViewInteraction getStepsHeader(){
        return onView(Matchers.allOf(hasSibling(withId(R.id.steps_recycler_view)), withText(R.string.steps_header)));
    }

    public static ViewInteraction getStepsRecyclerView(){
        return onView(withId(R.id.steps_recycler_view));
    }

    public static ViewInteraction getIngredientRecyclerView(){
        return onView(withId(R.id.ingredient_recycler_view));
    }

    public static ViewInteraction getPrevButton(){
        return onView(withId(R.id.previous_step_button));
    }
    public static ViewInteraction getNextButton(){
        return onView(withId(R.id.next_step_button));
    }

    public static ViewInteraction getStepDescription(){
        return onView(withId(R.id.steps_description_tv));
    }
    public static ViewInteraction getExoPlayer(){
        return onView(withId(R.id.video_view));
    }
}
