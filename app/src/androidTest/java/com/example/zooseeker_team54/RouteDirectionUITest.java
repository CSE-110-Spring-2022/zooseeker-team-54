package com.example.zooseeker_team54;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
// User Story 3 and 4 UI Test
public class RouteDirectionUITest {
    LocItemDao dao;
    LocDatabase testDb;
    Intent mainIntent;

    @Before
    public void setUp() {
        mainIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        ActivityScenario<MainActivity> mainActivityActivityScenario = ActivityScenario.launch(mainIntent);
        mainActivityActivityScenario.onActivity(Utilities::loadOldZooJson);
    }


    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, LocDatabase.class)
                .allowMainThreadQueries()
                .build();
        LocDatabase.injectTestDatabase(testDb);

        List<LocItem> todos = LocItem.loadJSON(context, "sample_node_info.json");
        dao = testDb.LocItemDao();
        dao.insertAll(todos);
    }

    /**
     * Selects 3 exhibits (Lions, Alligators, and Arctic Foxes) and plans a route.
     */
    @Test
    public void routeDirectionUITest() {
        ViewInteraction clearButton = onView(
                allOf(withId(R.id.clear_btn), withText("clear"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        clearButton.perform(click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.search_bar),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(replaceText("lions"), closeSoftKeyboard());

        ViewInteraction materialAutoCompleteTextView2 = onView(
                allOf(withId(R.id.search_bar), withText("lions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView2.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.loc_name), withText("Lions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction materialAutoCompleteTextView3 = onView(
                allOf(withId(R.id.search_bar), withText("lions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView3.perform(replaceText("gators"));

        ViewInteraction materialAutoCompleteTextView4 = onView(
                allOf(withId(R.id.search_bar), withText("gators"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView4.perform(closeSoftKeyboard());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.loc_name), withText("Alligators"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction materialAutoCompleteTextView5 = onView(
                allOf(withId(R.id.search_bar), withText("gators"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView5.perform(replaceText("foxes"));

        ViewInteraction materialAutoCompleteTextView6 = onView(
                allOf(withId(R.id.search_bar), withText("foxes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialAutoCompleteTextView6.perform(closeSoftKeyboard());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.loc_name), withText("Arctic Foxes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_results),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_btn), withText("plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.loc_name), withText("Alligators"),
                        withParent(withParent(withId(R.id.planned_route))),
                        isDisplayed()));
        textView.check(matches(withText("Alligators")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.loc_name), withText("Lions"),
                        withParent(withParent(withId(R.id.planned_route))),
                        isDisplayed()));
        textView2.check(matches(withText("Lions")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.loc_name), withText("Arctic Foxes"),
                        withParent(withParent(withId(R.id.planned_route))),
                        isDisplayed()));
        textView3.check(matches(withText("Arctic Foxes")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.loc_name), withText("Arctic Foxes"),
                        withParent(withParent(withId(R.id.planned_route))),
                        isDisplayed()));
        textView4.check(matches(withText("Arctic Foxes")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
