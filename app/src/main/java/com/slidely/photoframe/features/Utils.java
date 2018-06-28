package com.slidely.photoframe.features;

import android.app.Activity;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class Utils {
    public static void hideBars(@NotNull Activity activity) {
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }
}