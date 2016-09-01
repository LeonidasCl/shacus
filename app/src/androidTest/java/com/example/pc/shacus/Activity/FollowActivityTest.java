package com.example.pc.shacus.Activity;

import android.support.test.rule.ActivityTestRule;

import com.example.pc.shacus.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by cuicui on 2016/8/30.
 */

public class FollowActivityTest {

    @Rule
    public ActivityTestRule<FollowActivity> activityTestRule = new ActivityTestRule<>(FollowActivity.class);

    @Test
    public void testOfFollowActivity() throws InterruptedException {
        //点击返回按钮
        onView(withId(R.id.backbtn)).perform(click());
        Thread.sleep(2000);

        //查看title文字是否显示
        onView(withId(R.id.follow)).check(matches(withText("follow")));
        Thread.sleep(2000);
    }

}