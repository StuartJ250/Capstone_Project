package com.example.expensetracker.login;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.dao.LoginDAO;
import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.entities.Login;
import com.example.expensetracker.ui.login.LoginActivity;


import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

    private ExpenseDatabase expenseDatabase;
    private LoginDAO loginDAO;

    @Before
    public void setup(){
        Context context = ApplicationProvider.getApplicationContext();
        expenseDatabase = Room.inMemoryDatabaseBuilder(context,ExpenseDatabase.class)
                .allowMainThreadQueries()
                .build();

        ExpenseDatabase.setTestInstance(expenseDatabase);

        loginDAO = expenseDatabase.loginDAO();

        loginDAO.insert(new Login("validuser", "correctpassword", "test", "login"));
    }

    @Before
    public void initIntents(){
        Intents.init();
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void a_testSanity() {
        System.out.println("✅ testSanity is running");
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }


    @Test
    public void b_testEmptyLogin_showError() {
        onView(withId(R.id.userName)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.btnLogin)).perform(click());

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Invalid Username or Password"))
                .check(matches(isDisplayed()));
        System.out.println("Error toast displayed as expected");
    }

    @Test
    public void c_testCorrectLogin_showSuccess(){
        onView(withId(R.id.userName)).perform(typeText("validuser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("correctpassword"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        try{
            Thread.sleep(1500);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        onView(withText("Login Successful!"))
                .check(matches(isDisplayed()));
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        intended(hasComponent(MainActivity.class.getName()));
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @After
    public void eraseTest() {
        ExpenseDatabase.setTestInstance(null);
    }
}