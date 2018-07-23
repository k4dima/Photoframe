package com.k4dima.photoframe

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.k4dima.photoframe.features.main.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @JvmField
    @Rule
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSlideShow() {
        Thread.sleep(4000)
    }
}