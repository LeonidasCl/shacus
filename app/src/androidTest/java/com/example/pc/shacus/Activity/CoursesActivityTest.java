package com.example.pc.shacus.Activity;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pc.shacus.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by LQ on 2016/8/29.
 */
@RunWith(AndroidJUnit4.class)
public class CoursesActivityTest {

    //测试规则
    @Rule
    public ActivityTestRule<CoursesActivity> activityRule = new ActivityTestRule<>(CoursesActivity.class);

    @Test
    public void testOnCreate() throws Exception {
        onView(withId(R.id.btn_finishList)).perform(click());

        onView(withId(R.id.btn_finishList)).check(matches(withText("完成列表")));


        onView(withId(R.id.listView_course)).perform(click());
        Thread.sleep(25000);
    }
}