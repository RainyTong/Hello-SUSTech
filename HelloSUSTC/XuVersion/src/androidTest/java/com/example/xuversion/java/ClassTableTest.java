package com.example.xuversion.java;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.annotation.SuppressLint;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.example.xuversion.R;
import com.example.xuversion.main_activity.MainActivity;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ClassTableTest {
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex;
            int viewObjHash;

            @SuppressLint("DefaultLocale") @Override
            public void describeTo(Description description) {
                description.appendText(String.format("with index: %d ", index));
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode();
                }
                return view.hashCode() == viewObjHash;
            }
        };
    }

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void OpenTableCheck() throws Exception{
        try {
            LogIn();
        }catch (PerformException p){

        }
        OpenClassTable();
        CheckTablePage();
        onView(ViewMatchers.withId(R.id.layout_return)).perform(click());
        onView(ViewMatchers.withId(R.id.textView)).check(matches(withText("Hello SUSTech")));
    }
    @Test
    public void AddTabCancleTest(){
        try {
            LogIn();
        }catch (PerformException p){

        }
        OpenClassTable();
        OpenAddTab();
        onView(ViewMatchers.withId(R.id.classcancel)).perform(click());
        CheckTablePage();
    }

    @Test
    public void AddTabUpdateTest(){
        try {
            LogIn();
        }catch (PerformException p){

        }
        OpenClassTable();
        OpenAddTab();
        onView(ViewMatchers.withText("更新")).perform(click());
        onView(withIndex(withText("课程：test"),0)).perform(click());
        onView(ViewMatchers.withText("课程：test")).perform(longClick());
        onView(ViewMatchers.withText("删除")).perform(click());
        OpenAddTab();
    }

    public void CheckTablePage(){
        onView(ViewMatchers.withId(R.id.layout_name)).check(matches(withText("2019Summer")));
    }

    public void OpenAddTab(){
        onView(withIndex(withText("08:00-09:50"), 0)).perform(longClick());
    }

    public void OpenClassTable(){
        onView(ViewMatchers.withId(R.id.main_class_button)).perform(click());
    }

    public void LogIn(){
        onView(ViewMatchers.withId(R.id.main_signin_btn)).perform(click());
        onView(ViewMatchers.withId(R.id.login_input_username))
                .perform(typeText("123"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.login_input_password))
                .perform(typeText("123"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.main_btn_login))
                .perform(click());
    }

}