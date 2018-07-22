package com.k4dima.photoframe.features.main.domain

import androidx.lifecycle.MutableLiveData
import com.k4dima.photoframe.features.main.data.MediaRepository
import com.k4dima.photoframe.features.main.data.PreferenceRepository
import com.k4dima.photoframe.features.main.data.model.Media
import com.k4dima.photoframe.features.main.ui.di.MainScope
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

@MainScope
class MediaUseCase
@Inject
constructor(private val mediaRepository: MediaRepository, preferenceRepository: PreferenceRepository) : UseCase {
    companion object {
        private const val MIN_SAFE_NUMBER = 10
    }

    override val mediaLiveData: MutableLiveData<Media> = object : MutableLiveData<Media>() {
        private lateinit var timer: Timer
        private lateinit var disposable: Disposable
        override fun onActive() {
            disposable = preferenceRepository.speed.subscribe(Consumer {
                timer = Timer().apply {
                    schedule(timerTask { item() }, 0, it.toLong())
                }
            })
        }

        override fun onInactive() {
            disposable.dispose()
            timer.cancel()
        }
    }
    private val deque = LinkedList<Media>()
    private var currentPosition: Int = 0
    private var total: Int = 0

    fun item() {
        if (deque.isEmpty()) {
            mediaRepository.media(0).subscribe(Consumer {
                deque += it
                if (it.isEmpty())
                    mediaLiveData.postValue(null)
                else
                    item()
            })
        } else {
            if (deque.size == MIN_SAFE_NUMBER) {
                val offset = currentPosition + MIN_SAFE_NUMBER
                mediaRepository.media(offset).subscribe(Consumer {
                    if (it.isEmpty()) {
                        total = offset
                        mediaRepository.media(0).subscribe(Consumer { deque += it })
                    }
                    deque += it
                })
            }
            val newMedia = deque.removeFirst()
            if (total != 0 && currentPosition == total - 1) {
                currentPosition = 0
            } else {
                currentPosition++
            }
            println("********")
            println(newMedia.uri)
            println("********")
            mediaLiveData.postValue(newMedia)
        }
    }
}