package com.hua.softkeyboard_core;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * 类似系统键盘，在弹出的时候会把TargetView往上顶。
 *
 * @author zhangsh
 * @version V1.0
 * @date 2019-11-28 10:58
 */

public class LikeSoftKeyboard {

    private static SparseArray<ILikeSoftKeyboardTheme> sRegisteredThemeArray = new SparseArray<>();

    private static WeakReference<LikeSoftKeyboardPopup> mPopupWindow;

    private static int sLastBottom = -1;

    public static void registerTheme(ILikeSoftKeyboardTheme theme) {
        sRegisteredThemeArray.put(theme.themeId(), theme);
    }

    public static boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.get() != null &&
                mPopupWindow.get().mIsShowing;
    }

    public static void show(final View targetView, final int themeId) {
        final Activity activity = (Activity) targetView.getContext();
        // 等待系统键盘关闭
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showInternal(activity, targetView, themeId);
            }
        }, 200);
    }

    private static void showInternal(Activity context, final View targetView, int themeId) {
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
        dismiss(true);
    }

    public static void dismiss(boolean resetScroll) {
        LikeSoftKeyboardPopup popupWindow = mPopupWindow.get();
        if (popupWindow != null) {
            popupWindow.dismissInternal(resetScroll);
        }
    }

    private static class LikeSoftKeyboardPopup extends PopupWindow {
        private View mVisibleView;
        private static final int SCROLL_DURATION = 300;
        private ValueAnimator scrollAnimator;
        private int tempScrollBy;
        private View mDecorView;
        private boolean mResetScroll = true;
        private int mTargetResetBottom;
        private boolean mIsShowing = false;

        private LikeSoftKeyboardPopup(Context context) {
            super(context);
            // setFocusable(true);
            setOutsideTouchable(true);
            setBackgroundDrawable(new ColorDrawable(0));
            setWidth(-1);
            setHeight(-2);
            setAnimationStyle(R.style.LiveSoftKeyboard_Animation);
        }

        private void show(View visibleView) {
            mVisibleView = visibleView;
            showAtLocation(mVisibleView, Gravity.BOTTOM, 0, 0);
            scrollWindowEnsureVisible();
            mIsShowing = true;
        }

        private void dismissInternal(boolean resetScroll) {
            mResetScroll = resetScroll;
            dismiss();
        }

        @Override
        public void dismiss() {
            super.dismiss();
            scrollAnimator.cancel();
            if (mResetScroll) {
                int curBottom = Utils.getViewBottomOnScreen(mVisibleView);
                scrollContainerWithOffset(curBottom - mTargetResetBottom);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsShowing = false;
                    }
                }, 300);
            } else {
                mIsShowing = false;
            }
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
                    mTargetResetBottom = targetBottom;
                    scrollContainerWithOffset(offset);
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
                }
            });
            scrollAnimator.start();
        }
    }

}
