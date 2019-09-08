package com.chandu.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class BakingActivityBasicTest {
    private static final String RECIPE_NUTELLAPIE = "Nutella Pie";
    private static final String RECIPE_BROWNIES = "Brownies";
    private static final String RECIPE_YELLOWCAKE = "Yellow Cake";
    private static final String RECIPE_CHEESECAKE = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    public static void pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
    }

    public String getRecipeName(int id) {
        switch (id) {
            case 0:
                return RECIPE_NUTELLAPIE;
            case 1:
                return RECIPE_BROWNIES;
            case 2:
                return RECIPE_YELLOWCAKE;
            case 3:
                return RECIPE_CHEESECAKE;
            default:
                return null;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /*
     * Verify all the different recipes are displayed
     */
    @Test
    public void verifyListButtons() {
        boolean bigPane = isTablet(mActivityTestRule.getActivity().getApplicationContext());
        //Check Nutella Pie
        for (int i = 0; i < 4; i ++) {
            onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(i, click()));
            if (bigPane) {
                onView(withId(R.id.recipe_ingredient)).perform(click());
            }

            pressBack();
        }
    }

    /*
     * Verify correct ingredient is displayed
     */
    @Test
    public void verifyIngredients() {
        onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_ingredient)).perform(click());
        onView(allOf(instanceOf(TextView.class), withText(RECIPE_NUTELLAPIE + " Ingredients"))).check(matches(isDisplayed()));
    }

    /*
     * Verify exoplayer is displayed
     */
    @Test
    public void verifyExoPlayer() {
        boolean bigPane = isTablet(mActivityTestRule.getActivity().getApplicationContext());
        //Check Nutella Pie
        for (int i = 0; i < 4; i ++) {
            onView(withId(R.id.recyclerview_recipelist)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(i, click()));
            onData(anything()).inAdapterView(withId(R.id.recipe_steps_list)).atPosition(0).perform(click());
            onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.video_exoplayer_view))).check(matches(isDisplayed()));
            if (!bigPane) {
                pressBack();
            }

            pressBack();
        }
    }
}
