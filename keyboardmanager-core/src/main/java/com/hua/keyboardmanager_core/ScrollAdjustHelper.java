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
    private View popupView;
    private View containerView;
    private ValueAnimator scrollAnimator;
    private int containerScrollY;
    private int tempScrollBy;

    public ScrollAdjustHelper(View targetView, View popupView) {
        this.targetView = targetView;
        this.popupView = popupView;
        this.containerScrollY = 0;
    }

    public void updateView(View targetView, View popupView) {
        this.targetView = targetView;
        this.popupView = popupView;
        this.containerScrollY = 0;
    }

    public void adjust() {
        int offset = getScrollOffset(targetView, popupView);
        if (containerView == null) {
            containerView = findActivityContentViewGroup(targetView);
        }
        if (containerView != null) {
            scrollContainerWithOffset(containerView, offset - containerScrollY);
        }
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

    private static int getScrollOffset(View targetView, View popupView) {
        int[] tempLocation = new int[2];
        targetView.getLocationOnScreen(tempLocation);
        int focusBottom = tempLocation[1] + targetView.getHeight();
        popupView.getLocationOnScreen(tempLocation);
        int popupTop = tempLocation[1];
        int offset = focusBottom - popupTop;
        return offset > 0 ? offset : 0;
    }

}
