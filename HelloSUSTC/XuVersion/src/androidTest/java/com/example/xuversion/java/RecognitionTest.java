package com.example.xuversion.java;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.xuversion.R;
import com.example.xuversion.main_activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecognitionTest {
    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void openCamera(){
        onView(ViewMatchers.withId(R.id.main_building_button)).perform(click());
        onView(ViewMatchers.withId(R.id.layout_name)).check(matches(withText("Tensorflow")));
    }


}