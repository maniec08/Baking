package com.mani.baking.activity.utils;

import android.view.View;

import com.mani.baking.R;


import androidx.test.espresso.ViewAssertion;

import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


public class Assertion {

    public static ViewAssertion hasText(String text){
        return matches(withText(text));
    }
    public static ViewAssertion getIngredientHeaderAssertion(){
        return isCompletelyAbove(withText(R.string.steps_header));
    }

    public static ViewAssertion getStepsHeaderAssertion(){
        return isCompletelyAbove(withId(R.id.steps_recycler_view));
    }
    public static ViewAssertion getRowAssertion(String text){
        return matches(hasDescendant(withText(text)));
    }

    public static ViewAssertion isDisabled(){
        return matches(not(isEnabled()));
    }
    public static ViewAssertion isViewEnabled() {
        return matches(isEnabled());
    }
}
