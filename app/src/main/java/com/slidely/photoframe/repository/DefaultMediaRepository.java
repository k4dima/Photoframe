package com.slidely.photoframe.repository;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;

import com.slidely.photoframe.model.Media;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class DefaultMediaRepository implements MediaRepository {
    private final AppCompatActivity activity;
    private LoaderManager loaderManager;

    DefaultMediaRepository(@NotNull AppCompatActivity activity) {
        this.activity = activity;
        loaderManager = LoaderManager.getInstance(activity);
    }

    public void media(int offset, CompletedListener<List<Media>> completedListener) {
        loaderManager.initLoader((int) System.currentTimeMillis(), null,
                new MediaLoader(offset, completedListener));
    }

    class MediaLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final int LIMIT = 20;
        private Uri uri;
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;
        private CompletedListener<List<Media>> completedListener;

        MediaLoader(int offset, CompletedListener<List<Media>> completedListener) {
            this.completedListener = completedListener;
            uri = MediaStore.Files.getContentUri("external");
            selection = "("
                    + FileColumns.MEDIA_TYPE + "="
                    + FileColumns.MEDIA_TYPE_IMAGE
                    + " OR "
                    + FileColumns.MEDIA_TYPE + "="
                    + FileColumns.MEDIA_TYPE_VIDEO
                    + ")"
                    + " AND "
                    + ImageColumns.BUCKET_DISPLAY_NAME
                    + " = ?";
            selectionArgs = new String[]{"Camera"};
            sortOrder = ImageColumns.DATE_TAKEN + " DESC " +
                    " LIMIT " + LIMIT + " offset " + offset;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            return new CursorLoader(activity.getApplicationContext(), uri, null, selection,
                    selectionArgs, sortOrder);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, @NotNull Cursor cursor) {
            @SuppressLint("UseSparseArrays") List<Media> list = new LinkedList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(FileColumns._ID));
                int media = cursor.getInt(cursor.getColumnIndexOrThrow(FileColumns.MEDIA_TYPE));
                Uri uri = Uri.withAppendedPath(this.uri, "" + id);
                list.add(new Media(uri.toString(), media == FileColumns.MEDIA_TYPE_VIDEO));
            }
            cursor.close();
            completedListener.completed(list);
            loaderManager.destroyLoader(loader.getId());
        }
    }
}