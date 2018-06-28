package com.slidely.photoframe.features.media;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.slidely.photoframe.R;
import com.slidely.photoframe.features.Utils;
import com.slidely.photoframe.features.media.fragments.ImageFragment;
import com.slidely.photoframe.features.media.fragments.VideoFragment;
import com.slidely.photoframe.features.preferences.PreferencesActivity;
import com.slidely.photoframe.model.Media;
import com.slidely.photoframe.repository.RepositoryProvider;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

public class MediaActivity extends AppCompatActivity implements MediaView {
    public static final String URI_KEY = "uri";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());*/
        setContentView(R.layout.activity_main);
        CustomAnalogClock customAnalogClock = findViewById(R.id.analog_clock);
        customAnalogClock.setAutoUpdate(true);
        customAnalogClock.init(this, R.drawable.watch_face, R.drawable.hour_hand,
                R.drawable.minute_hand, 0, false, false);
        customAnalogClock.setOnClickListener(new View.OnClickListener() {
            private SparseIntArray map = new SparseIntArray();
            private int clockPosition;

            {
                map.put(0, Gravity.BOTTOM | Gravity.START);
                map.put(1, Gravity.TOP | Gravity.START);
                map.put(2, Gravity.TOP | Gravity.END);
                map.put(3, Gravity.CENTER);
                map.put(4, Gravity.BOTTOM | Gravity.END);
            }

            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView());
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.gravity = map.get(clockPosition);
                clockPosition = clockPosition == 4 ? 0 : clockPosition + 1;
                view.setLayoutParams(layoutParams);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        //noinspection ConstantConditions
        actionBar.hide();
        findViewById(R.id.media_fragment).setOnClickListener((v) -> {
            if (actionBar.isShowing())
                actionBar.hide();
            else
                actionBar.show();
        });
        Toast toast = Toast.makeText(this, "Touch screen to access settings. Touch clock to change position",
                Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideBars(this);
        boolean permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (permission) {
            startSlideShow();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startSlideShow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        } else {
            return false;
        }
    }

    private void startSlideShow() {
        ViewModelProviders.of(this,
                new MediaViewModelFactory(this, RepositoryProvider.provideMediaRepository(this),
                        RepositoryProvider.providePreferenceRepository(this))).get(MediaViewModel.class);
    }

    @Override
    public void showMedia(@Nullable Media media) {
        if (media != null) {
            Boolean video = media.isVideo();
            Bundle bundle = new Bundle();
            String uri = media.getUri();
            bundle.putString(URI_KEY, uri);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;
            fragment = Fragment.instantiate(this, video
                    ? VideoFragment.class.getName()
                    : ImageFragment.class.getName(), bundle);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.media_fragment, fragment, uri)
                    .commit();
        } else {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void postDelayed(Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    @Override
    public void removeCallbacks(Runnable r) {
        handler.removeCallbacks(r);
    }
}