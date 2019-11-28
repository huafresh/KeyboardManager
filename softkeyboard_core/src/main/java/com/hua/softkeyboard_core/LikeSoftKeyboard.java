package com.hua.softkeyboard_core;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * 弹出时会保证指定的TargetView是可见的，等同于系统键盘弹出时把EditText往上顶的效果。
 *
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 10:58
 */

public class LikeSoftKeyboard {

    private static SparseArray<ILikeSoftKeyboardTheme> sRegisteredThemeArray = new SparseArray<>();

    private static WeakReference<LikeSoftKeyboardPopup> mPopupWindow;

    public static void registerTheme(ILikeSoftKeyboardTheme theme) {
        sRegisteredThemeArray.put(theme.themeId(), theme);
    }

    public static void show(Context context, final View targetView, int themeId) {
        if (mPopupWindow != null) {
            LikeSoftKeyboardPopup popupWindow = mPopupWindow.get();
            if (popupWindow != null) {
                if (!popupWindow.isShowing()) {
                    popupWindow.show(targetView);
                }
                return;
            }
        }

        ILikeSoftKeyboardTheme softKeyboardTheme = sRegisteredThemeArray.get(themeId);
        if (softKeyboardTheme == null) {
            throw new IllegalArgumentException("themeId: " + themeId + " not exist. call registerTheme() first");
        }
        final FrameLayout container = new FrameLayout(context);
        View softView = softKeyboardTheme.createView(context, LayoutInflater.from(context), container);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        if (container.getChildCount() == 0) {
            container.addView(softView, params);
        }

        LikeSoftKeyboardPopup popup = new LikeSoftKeyboardPopup(context);
        popup.setContentView(container);
        mPopupWindow = new WeakReference<>(popup);
        popup.show(targetView);
    }

    private static @Nullable
    View findWindowDecorView(View targetView) {
        if (targetView.getContext() instanceof Activity) {
            Activity activity = (Activity) targetView.getContext();
            return activity.getWindow().getDecorView().findViewById(android.R.id.content);
        }
        return null;
    }

    public static void dismiss() {
        LikeSoftKeyboardPopup popupWindow = mPopupWindow.get();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private static class LikeSoftKeyboardPopup extends PopupWindow {
        private View mVisibleView;
        private int mTotalScrollY;
        private static final int SCROLL_DURATION = 150;
        private ValueAnimator scrollAnimator;
        private int tempScrollBy;
        private View mDecorView;

        private LikeSoftKeyboardPopup(Context context) {
            super(context);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(0));
            setWidth(-1);
            setHeight(-2);
            setAnimationStyle(-1);
        }

        private void show(View visibleView) {
            mVisibleView = visibleView;
            showAtLocation(mVisibleView, Gravity.BOTTOM, 0, 0);
            scrollWindowEnsureVisible();
        }

        @Override
        public void dismiss() {
            super.dismiss();
            scrollAnimator.cancel();
            scrollContainerWithOffset(-mTotalScrollY);
        }

        private void scrollWindowEnsureVisible() {
            mDecorView = findWindowDecorView(mVisibleView);
            if (mDecorView == null) {
                return;
            }
            getContentView().post(new Runnable() {
                @Override
                public void run() {
                    int popupTop = Utils.getViewTopOnScreen(getContentView());
                    int targetBottom = Utils.getViewBottomOnScreen(mVisibleView);
                    int offset = targetBottom - popupTop;
                    if (offset > 0) {
                        scrollContainerWithOffset(offset);
                    }
                }
            });
        }

        private void scrollContainerWithOffset(int offset) {

            if (scrollAnimator == null) {
                scrollAnimator = new ValueAnimator();
                scrollAnimator.setDuration(SCROLL_DURATION);
            }

            if (scrollAnimator.isRunning()) {
                scrollAnimator.cancel();
            }

            scrollAnimator.removeAllUpdateListeners();

            tempScrollBy = 0;
            scrollAnimator.setIntValues(0, offset);
            scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int curOffset = (int) animation.getAnimatedValue();
                    int scrollBy = curOffset - tempScrollBy;
                    mDecorView.scrollBy(0, scrollBy);
                    tempScrollBy += scrollBy;
                    mTotalScrollY += scrollBy;
                }
            });
            scrollAnimator.start();
        }
    }

}
