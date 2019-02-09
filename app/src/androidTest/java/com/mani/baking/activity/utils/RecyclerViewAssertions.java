package com.mani.baking.activity.utils;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotNull;

public class RecyclerViewAssertions {

    /**
     * Provides a RecyclerView assertion based on a view matcher. This allows you to
     * validate whether a RecyclerView contains a row in memory without scrolling the list.
     *
     * @param viewMatcher - an Espresso ViewMatcher for a descendant of any row in the recycler.
     * @return an Espresso ViewAssertion to check against a RecyclerView.
     */
    public static ViewAssertion withRowContaining(final Matcher<View> viewMatcher) {
        assertNotNull(viewMatcher);

        return (view, noViewException) -> {
            if (noViewException != null) {
                throw noViewException;
            }

            assertTrue(view instanceof RecyclerView);

            RecyclerView recyclerView = (RecyclerView) view;
            final RecyclerView.Adapter adapter = recyclerView.getAdapter();
            for (int position = 0; position < adapter.getItemCount(); position++) {
                int itemType = adapter.getItemViewType(position);
                RecyclerView.ViewHolder viewHolder = adapter.createViewHolder(recyclerView, itemType);
                adapter.bindViewHolder(viewHolder, position);

                if (viewHolderMatcher(hasDescendant(viewMatcher)).matches(viewHolder)) {
                    return; // Found a matching row
                }
            }

            fail("No match found");
        };
    }

    /**
     * Creates matcher for view holder with given item view matcher.
     *
     * @param itemViewMatcher a item view matcher which is used to match item.
     * @return a matcher which matches a view holder containing item matching itemViewMatcher.
     */
    private static Matcher<RecyclerView.ViewHolder> viewHolderMatcher(final Matcher<View> itemViewMatcher) {
        return new TypeSafeMatcher<RecyclerView.ViewHolder>() {

            @Override
            public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
                return itemViewMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("holder with view: ");
                itemViewMatcher.describeTo(description);
            }
        };
    }
}
