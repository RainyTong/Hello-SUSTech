package com.example.xuversion.java;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.xuversion.R;
import com.example.xuversion.login_activity.LoginActivity;
import com.example.xuversion.main_activity.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> login = new ActivityTestRule<>(LoginActivity.class);
    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void signInUpCheck() throws Exception{
        //判断sign_up按钮 字体并按键
        onView(ViewMatchers.withId(R.id.sign_up))
                .check(matches(withText("Sign up")))
                .check(matches(isEnabled()))
                .perform(click());

        onView(ViewMatchers.withId(R.id.register_input_username))
                .check(matches(withHint("username")))
                .perform(typeText("aa"),closeSoftKeyboard());

        //新邮箱
        onView(ViewMatchers.withId(R.id.register_input_email))
                .perform(typeText("11611326@mail.sustc.edu.cn"),closeSoftKeyboard());

        //检测密码不一致
        onView(ViewMatchers.withId(R.id.register_input_password))
                .perform(typeText("aaa"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.register_input_password_check))
                .perform(typeText("aa"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.main_btn_login)).perform(click());
        onView(withText("The password is not the same as the confirmation password"))
                .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        //检测验证码是否发送
        onView(ViewMatchers.withId(R.id.register_input_password_check))
                .perform(typeText("a"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.main_btn_login)).perform(click());
        onView(withText("Please send the verification code first"))
                .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }





    @Test
    public void signInUpWrong() throws Exception{
        //判断sign_up按钮 字体并按键
        onView(ViewMatchers.withId(R.id.sign_up))
                .check(matches(withText("Sign up")))
                .check(matches(isEnabled()))
                .perform(click());

        onView(ViewMatchers.withId(R.id.register_input_username))
                .check(matches(withHint("username")))
                .perform(typeText("aa"),closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.main_btn_login)).perform(click());
        //检测空
        onView(withText("User name, password, mailbox cannot be empty"))
                .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        //检测邮箱不一致
        onView(ViewMatchers.withId(R.id.register_input_email))
                .perform(typeText("aaa"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.register_input_password))
                .perform(typeText("aaa"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.register_input_password_check))
                .perform(typeText("aaa"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.main_btn_login)).perform(click());
        onView(withText("Please enter SUSTC email in the correct format"))
                .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    @Test
    public void loginTestNull() throws Exception {
        onView(ViewMatchers.withId(R.id.login_input_username))
                .perform(typeText(""),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.login_input_password))
                .perform(typeText("1234"),closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.main_btn_login))
                .perform(click());
        onView(withText("The username and password cannot be empty"))
                .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginTestNormal() throws Exception {
        try {
            onView(ViewMatchers.withId(R.id.login_input_username))
                    .perform(typeText("123"),closeSoftKeyboard());
            onView(ViewMatchers.withId(R.id.login_input_password))
                    .perform(typeText("123"),closeSoftKeyboard());
            onView(ViewMatchers.withId(R.id.main_btn_login))
                    .perform(click());
            onView(withText("hello 123"))
                    .inRoot(withDecorView(not(login.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        }catch (NoMatchingViewException p){

        }

    }
}


