package com.example.expensetracker.register;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

import static com.google.android.material.R.id.snackbar_text;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.ui.login.LoginActivity;
import com.example.expensetracker.ui.register.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterActivityTest {


    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void initIntents(){
        Intents.init();
    }

    @Test
    public void a_testSanity() {
        System.out.println("✅ testSanity is running");
        onView(withId(R.id.btnNewRegister)).check(matches(isDisplayed()));
    }


    @Test
    public void b_testEmptyRegister_showError() {
        onView(withId(R.id.userName)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.firstName)).perform(clearText());
        onView(withId(R.id.lastName)).perform(clearText());
        onView(withId(R.id.btnNewRegister)).perform(click());

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Please complete all requested forms"))
                .check(matches(isDisplayed()));
        System.out.println("Error toast displayed as expected");
    }

    @Test
    public void c_testCorrectRegister_showSuccess(){

        onView(withId(R.id.userName)).perform(typeText("newuser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("newpassword"), closeSoftKeyboard());
        onView(withId(R.id.firstName)).perform(typeText("new"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("user"), closeSoftKeyboard());
        onView(withId(R.id.btnNewRegister)).perform(click());

        onView(withText("Registration Successful!"))
                .check(matches(isDisplayed()));
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        intended(hasComponent(LoginActivity.class.getName()));

    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

}
