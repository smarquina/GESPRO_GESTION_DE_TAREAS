/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.davidmiguel.gobees.apiaries;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.splash.SplashActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Delete apiary test.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteApiaryTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

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

    @Test
    public void deleteApiaryTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_apiary),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.add_apiary_name));
        appCompatEditText.perform(scrollTo(), replaceText("Apiary"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab_add_apiary),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.apiary_name), withText("Apiary"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Apiary")));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.more_icon), withContentDescription("More…"), isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Delete"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.card),
                        childAtPosition(
                                allOf(withId(R.id.apiaries_list),
                                        childAtPosition(
                                                withId(R.id.apiariesLL),
                                                0)),
                                0),
                        isDisplayed()));
        frameLayout.check(doesNotExist());

        ViewInteraction textView2 = onView(
                withId(R.id.no_apiaries_text));
        textView2.check(matches(isDisplayed()));

    }
}
