package com.hua.keyboardmanager_core;

import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 9:24
 */

public class ScrollAdjustHelper {

    public static int SCROLL_DURATION = 150;
    private View targetView;
    private View containerView;
    private ValueAnimator scrollAnimator;
    private int containerScrollY;
    private int tempScrollBy;
    private int limitY = -1;

    public ScrollAdjustHelper(View targetView, View popupView) {
        this.scrollContainerWithOffset(targetView, getViewTopOnScreen(popupView));
    }

    public ScrollAdjustHelper(View targetView, int limitY) {
        this.targetView = targetView;
        this.limitY = limitY;
        this.containerScrollY = 0;
    }

    public void update(View targetView, View popupView) {
        this.update(targetView, getViewTopOnScreen(popupView));
    }

    public void update(View targetView, int limitY) {
        this.targetView = targetView;
        this.limitY = limitY;
        this.containerScrollY = 0;
    }

    public void adjust() {
        int offset = getScrollOffset(targetView, limitY);
        if (containerView == null) {
            containerView = findActivityContentViewGroup(targetView);
        }
        if (containerView != null) {
            scrollContainerWithOffset(containerView, offset - containerScrollY);
        }
    }

    public static int getViewTopOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    private void scrollContainerWithOffset(final View containerView, final int offset) {
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
                containerView.scrollBy(0, scrollBy);
                tempScrollBy += scrollBy;
                containerScrollY += scrollBy;

//                Log.e("@@@hua", "scrollBy = " + scrollBy +
//                        ". tempScrollBy = " + tempScrollBy +
//                        ". containerScrollY = " + containerScrollY);
            }
        });
        scrollAnimator.start();
    }

    public void reset() {
        scrollContainerWithOffset(containerView, -containerScrollY);
    }

    private static @Nullable
    View findActivityContentViewGroup(View target) {
        if (target.getId() == android.R.id.content) {
            return target;
        }

        ViewParent parent = target.getParent();
        if (parent instanceof View) {
            return findActivityContentViewGroup((View) parent);
        }
        return null;
    }

    private static int getScrollOffset(View targetView, int limitY) {
        int[] tempLocation = new int[2];
        targetView.getLocationOnScreen(tempLocation);
        int focusBottom = tempLocation[1] + targetView.getHeight();
        int offset = focusBottom - limitY;
        return offset > 0 ? offset : 0;
    }

}
