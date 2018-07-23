package com.k4dima.photoframe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.k4dima.photoframe.features.main.data.MediaRepository
import com.k4dima.photoframe.features.main.data.PreferenceRepository
import com.k4dima.photoframe.features.main.data.model.Media
import com.k4dima.photoframe.features.main.domain.MediaUseCase
import io.reactivex.Single
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.*


@RunWith(JUnit4::class)
class MediaUseCaseTest {
    @JvmField
    @Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testUseCase() {
        val mediaRepository = Mockito.mock(MediaRepository::class.java)
        val preferenceRepository = Mockito.mock(PreferenceRepository::class.java)
        val mediaUseCase = MediaUseCase(mediaRepository, preferenceRepository)
        val list = LinkedList<Media>()
        Mockito.`when`(mediaRepository.media(0)).thenReturn(Single.just(list))
        Mockito.`when`(preferenceRepository.speed).thenReturn(Single.just(3))
        mediaUseCase.mediaLiveData.observeForever { assertNull(it) }
    }
}