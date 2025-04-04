package com.dset.expensetracker.expense;


import static androidx.test.espresso.Espresso.onData;
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.fragment.app.testing.FragmentScenario;

import com.dset.expensetracker.R;
import com.dset.expensetracker.database.ExpenseDatabase;
import com.dset.expensetracker.entities.Login;
import com.dset.expensetracker.ui.expense.ExpenseFragment;


import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExpenseFragmentTest {

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        ExpenseDatabase test = Room.inMemoryDatabaseBuilder(context, ExpenseDatabase.class)
                .allowMainThreadQueries().build();

        ExpenseDatabase.setTestInstance(test);

        test.loginDAO().insert(new Login("testlogin", "testpass", "test", "user"));

        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("userID", 1).apply();
    }

    @Test
    public void a_testSanity() {
        FragmentScenario<ExpenseFragment> scenario = FragmentScenario.launchInContainer(ExpenseFragment.class, null, R.style.Theme_ExpenseTracker);

        System.out.println("✅ testSanity is running");
        onView(withId(R.id.btnSubmitExpense)).check(matches(isDisplayed()));
    }

    @Test
    public void b_testEmptyExpense_showError() {
        FragmentScenario<ExpenseFragment> scenario = FragmentScenario.launchInContainer(ExpenseFragment.class, null, R.style.Theme_ExpenseTracker);

        onView(withId(R.id.expenseName)).perform(clearText());
        onView(withId(R.id.vendorName)).perform(clearText());
        onView(withId(R.id.description)).perform(clearText());
        onView(withId(R.id.amountSpent)).perform(clearText());
        onView(withId(R.id.btnSubmitExpense)).perform(click());

        try{
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Please complete the required fields"))
                .check(matches(isDisplayed()));

        System.out.println("Error toast displayed as expected");
    }

    @Test
    public void c_testCorrectExpense_showSuccess(){
        FragmentScenario<ExpenseFragment> scenario = FragmentScenario.launchInContainer(ExpenseFragment.class, null, R.style.Theme_ExpenseTracker);

        onView(withId(R.id.expenseName)).perform(typeText("Lunch"), closeSoftKeyboard());
        onView(withId(R.id.vendorName)).perform(typeText("Taco Bell"), closeSoftKeyboard());
        onView(withId(R.id.description)).perform(typeText("travel food"), closeSoftKeyboard());
        onView(withId(R.id.amountSpent)).perform(typeText("30.32"), closeSoftKeyboard());
        onView(withId(R.id.expenseDate)).perform(click());
        onView(withText("OK")).perform(click());


        onView(withId(R.id.spinnerCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Food"))).perform(click());
        onView(withId(R.id.spinnerPaymentMethod)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Cash"))).perform(click());

        onView(withId(R.id.btnSubmitExpense)).perform(click());

        try{
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Expense added successfully"))
                .check(matches(isDisplayed()));

    }

    @After
    public void tearDown() {
        ExpenseDatabase.getInstance(ApplicationProvider.getApplicationContext()).close();
        ExpenseDatabase.setTestInstance(null);
    }
}
