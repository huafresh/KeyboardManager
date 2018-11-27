package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.ArraySet;

import java.util.Set;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/27 13:46
 * @deprecated 系统键盘关闭其实是很快的，但是有动画的存在，所以看起来收起来的慢。
 */
@Deprecated
class SysKeyboardCloseUtil {

    static void waitSysKeyboardClosed(Activity activity, Runnable runnable) {
        new Task(activity, runnable).loop();
    }

    private static class Task implements ActivityCallbackHelper.LifecycleListener {
        private static final int WHAT_QUERY_CLOSED = 0;
        private static final int QUERY_DELAY_MILLIS = 50;
        private Activity activity;
        private Runnable runnable;
        private Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case WHAT_QUERY_CLOSED:
                        loop();
                        break;
                    default:
                        break;
                }
            }
        };

        Task(Activity activity, Runnable runnable) {
            this.activity = activity;
            this.runnable = runnable;
            ActivityCallbackHelper.doOnActivityDestroyed(activity, this);
        }

        void loop() {
            if (FlexKeyboardManager.isSysKeyboardShowing(activity)) {
                runnable.run();
            } else {
                Message message = handler.obtainMessage(WHAT_QUERY_CLOSED);
                handler.sendMessageDelayed(message, QUERY_DELAY_MILLIS);
            }
        }

        @Override
        public void onLifecycle(Activity activity) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
