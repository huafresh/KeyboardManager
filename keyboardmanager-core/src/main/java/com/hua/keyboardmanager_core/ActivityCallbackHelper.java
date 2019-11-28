package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/26
 */
class ActivityCallbackHelper implements Application.ActivityLifecycleCallbacks {

    private static boolean registered = false;
    private static HashMap<Activity, Set<LifecycleListener>> destroyListeners;

    private static void ensureRegisterActivityCallback(Application application) {
        if (!registered) {
            application.registerActivityLifecycleCallbacks(new ActivityCallbackHelper());
            registered = true;
        }
    }

    static void doOnActivityDestroyed(Activity activity, LifecycleListener listener) {
        ensureRegisterActivityCallback(activity.getApplication());

        if (destroyListeners == null) {
            destroyListeners = new HashMap<>();
        }

        if (destroyListeners.containsKey(activity)) {
            Set<LifecycleListener> listeners = destroyListeners.get(activity);
            listeners.add(listener);
        } else {
            Set<LifecycleListener> listeners = new ArraySet<>();
            listeners.add(listener);
            destroyListeners.put(activity, listeners);
        }
    }

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
        Set<LifecycleListener> listeners =  destroyListeners.remove(activity);
        if (listeners != null) {
            for (LifecycleListener listener : listeners) {
                listener.onLifecycle(activity);
            }
        }
    }

    interface LifecycleListener {
        void onLifecycle(Activity activity);
    }

}
