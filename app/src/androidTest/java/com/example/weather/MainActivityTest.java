package com.example.weather;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.weather.views.MainActivity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void whenLocationUnavailable_dialogIsShown() {
        ActivityScenario.launch(MainActivity.class);

        onView(withText("Location unavailable"))
                .check(matches(isDisplayed()));

        onView(withText("Enter city"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void changeLocationButton_opensCityInputActivity() {
        ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.btnChangeCity))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));

        onView(withId(R.id.btnChangeCity))
                .perform(click());

        onView(withId(R.id.editCity))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnConfirmCity))
                .check(matches(isDisplayed()));
    }
}
