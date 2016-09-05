package com.example.pc.shacus.Fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pc.shacus.Activity.ShareActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by 启凡 on 2016/9/1.
 */
@RunWith(AndroidJUnit4.class)
public class UserFragmentTest {
    //测试规则
    @Rule
    public ActivityTestRule<ShareActivity> activityRule = new ActivityTestRule<>(ShareActivity.class);

    @Test
    public void testOnCreate() throws Exception {
        Thread.sleep(10000);
    }
}