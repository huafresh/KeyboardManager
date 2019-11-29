package com.hua.softkeyboard_like;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

public class SoftKeyboardLike {

    private static SparseArray<ISoftKeyboardLikeLayoutTheme> sRegisteredThemeArray = new SparseArray<>();

    private static WeakReference<LikeSoftKeyboardPopup> mPopupWindow;

    public static void registerTheme(ISoftKeyboardLikeLayoutTheme layoutTheme) {
        sRegisteredThemeArray.put(layoutTheme.layoutThemeId(), layoutTheme);
    }

    public static boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.get() != null &&
                mPopupWindow.get().mIsShowing;
    }

    public static void show(final View targetView, final int layoutThemeId, long delay) {
        if (delay > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInternal(targetView, layoutThemeId);
                }
            }, delay);
        } else {
            showInternal(targetView, layoutThemeId);
        }
    }

    private static void showInternal(View targetView, int layoutThemeId) {
        final Activity context = (Activity) targetView.getContext();
        LikeSoftKeyboardPopup popupWindow = getPopupWindow();
        if (popupWindow != null) {
            if (!popupWindow.isShowing()) {
                popupWindow.show(targetView);
            }
            return;
        }

        ISoftKeyboardLikeLayoutTheme softKeyboardTheme = sRegisteredThemeArray.get(layoutThemeId);
        if (softKeyboardTheme == null) {
            throw new IllegalArgumentException("layoutThemeId: " + layoutThemeId + " not exist. call registerTheme() first");
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

    public static void dismiss(OnSoftKeyboardLikeDismissListener listener) {
        LikeSoftKeyboardPopup popupWindow = getPopupWindow();
        if (popupWindow != null) {
            popupWindow.dismissInternal(listener);
        } else {
            listener.onDismiss();
        }
    }

    private static @Nullable
    LikeSoftKeyboardPopup getPopupWindow() {
        if (mPopupWindow != null) {
            return mPopupWindow.get();
        }
        return null;
    }

    private static class LikeSoftKeyboardPopup extends PopupWindow {
        private View mVisibleView;
        private static final int SCROLL_DURATION = 300;
        private ValueAnimator scrollAnimator;
        private int tempScrollBy;
        private View mDecorView;
        private int mTargetResetBottom;
        // 因为存在动画，因此单独维护变量
        private boolean mIsShowing = false;
        private OnSoftKeyboardLikeDismissListener listener;

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

        private void dismissInternal(OnSoftKeyboardLikeDismissListener listener) {
            if (!isShowing() && !mIsShowing) {
                listener.onDismiss();
            } else {
                this.listener = listener;
            }
        }

        @Override
        public void dismiss() {
            super.dismiss();
            scrollAnimator.cancel();
            int curBottom = Utils.getViewBottomOnScreen(mVisibleView);
            scrollContainerWithOffset(curBottom - mTargetResetBottom);
            scrollAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onDismiss();
                    scrollAnimator.removeListener(this);
                }
            });
        }

        private void onDismiss() {
            mIsShowing = false;
            if (listener != null) {
                listener.onDismiss();
                listener = null;
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

        private static @Nullable
        View findWindowDecorView(View targetView) {
            if (targetView.getContext() instanceof Activity) {
                Activity activity = (Activity) targetView.getContext();
                return activity.getWindow().getDecorView();
            }
            return null;
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
