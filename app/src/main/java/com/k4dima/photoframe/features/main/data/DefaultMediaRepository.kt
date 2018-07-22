package com.k4dima.photoframe.features.main.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Images.ImageColumns
import com.k4dima.photoframe.features.main.data.model.Media
import com.k4dima.photoframe.features.main.ui.di.MainScope
import com.squareup.sqlbrite3.SqlBrite
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@MainScope
class DefaultMediaRepository
@Inject
internal constructor(private val contentResolver: ContentResolver, private val sqlBrite: SqlBrite) : MediaRepository {
    companion object {
        private const val LIMIT = 20
    }

    override fun media(offset: Int): Single<List<Media>> {
        val mediaStoreUri = MediaStore.Files.getContentUri("external")
        val selection = "(${FileColumns.MEDIA_TYPE}=${FileColumns.MEDIA_TYPE_IMAGE} " +
                "OR " +
                "${FileColumns.MEDIA_TYPE}=${FileColumns.MEDIA_TYPE_VIDEO}) " +
                "AND " +
                "${ImageColumns.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf("Camera")
        val sortOrder = "${ImageColumns.DATE_TAKEN} DESC LIMIT $LIMIT OFFSET $offset"
        return sqlBrite.wrapContentProvider(contentResolver, Schedulers.io())
                .createQuery(mediaStoreUri, null, selection, selectionArgs, sortOrder, false)
                .mapToList {
                    val id = it.getLong(it.getColumnIndexOrThrow(FileColumns._ID))
                    val media = it.getInt(it.getColumnIndexOrThrow(FileColumns.MEDIA_TYPE))
                    val mediaUri = Uri.withAppendedPath(mediaStoreUri, "" + id)
                    Media(mediaUri.toString(), media == FileColumns.MEDIA_TYPE_VIDEO)
                }.firstOrError()
    }
}