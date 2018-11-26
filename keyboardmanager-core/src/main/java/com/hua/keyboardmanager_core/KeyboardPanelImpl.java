package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/24
 */
class KeyboardPanelImpl implements IKeyboardPanel {
    private static boolean registered = false;
    private KeyboardPopup keyboardPopup;

    KeyboardPanelImpl() {

    }

    @Override
    public void show(Activity activity, int themeId, final View visibleView) {
        ensureActivityCallback(activity.getApplication());

        if (keyboardPopup == null || !keyboardPopup.isSameWindow(activity)) {
            keyboardPopup = new KeyboardPopup(activity, themeId);
        } else {
            keyboardPopup.setThemeId(themeId);
        }

        keyboardPopup.show(visibleView);
    }

    @Override
    public void dismiss() {
        if (keyboardPopup != null) {
            keyboardPopup.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return keyboardPopup != null && keyboardPopup.isShowing();
    }

    private void ensureActivityCallback(Application application) {
        if (!registered) {
            application.registerActivityLifecycleCallbacks(new ActivityCallback());
            registered = true;
        }
    }

    private class ActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (keyboardPopup != null && keyboardPopup.isSameWindow(activity)) {
                if (keyboardPopup.isShowing()) {
                    keyboardPopup.dismiss();
                }
                keyboardPopup = null;
            }
        }
    }


}
