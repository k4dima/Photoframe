package com.k4dima.photoframe

import com.k4dima.photoframe.features.main.data.DefaultMediaRepository
import com.k4dima.photoframe.features.main.ui.MainActivity
import com.squareup.sqlbrite3.SqlBrite
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class MediaRepositoryTest {
    @Test
    fun testRepository() {
        val activity = Robolectric.setupActivity(MainActivity::class.java)
        DefaultMediaRepository(activity.contentResolver, SqlBrite.Builder().build())
    }
}