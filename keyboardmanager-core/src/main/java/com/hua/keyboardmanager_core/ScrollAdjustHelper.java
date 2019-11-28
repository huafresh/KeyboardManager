package com.hua.keyboardmanager_core;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;

import com.android.thinkive.framework.util.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 9:24
 */

public class ScrollAdjustHelper {
    public static int SCROLL_DURATION = 150;
    private static HashMap<Activity, ScrollContainer> containers;
    private static ViewRecycler viewRecycler = new ViewRecycler();

    public static int getViewTopOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    public static void adjust(View targetView, int limitY) {
        ScrollContainer container = getContainer(targetView);
        container.adjust(targetView, limitY);
    }

    public static void reset(View targetView) {
        ScrollContainer container = getContainer(targetView);
        container.reset();
    }

    private static ScrollContainer getContainer(View targetView) {
        if (containers == null) {
            containers = new HashMap<>();
        }

        Context context = targetView.getContext();
        if (context instanceof Activity) {
            ScrollContainer scrollContainer = containers.get(context);
            if (scrollContainer == null) {
                scrollContainer = new ScrollContainer((Activity) context);
                containers.put((Activity) context, scrollContainer);
                ActivityCallbackHelper.doOnActivityDestroyed((Activity) context, viewRecycler);
            }
            return scrollContainer;
        } else {
            throw new IllegalArgumentException("context of " + targetView + " must be Activity!!!");
        }
    }


    private static class ScrollContainer {
        private Activity activity;
        private View containerView;
        private ValueAnimator scrollAnimator;
        private int containerScrollY;
        private int tempScrollBy;
        private int[] tempLocation = new int[2];
        private int containerTop;

        private ScrollContainer(Activity activity) {
            this.activity = activity;
            this.containerView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            this.containerScrollY = 0;
            containerView.getLocationOnScreen(tempLocation);
            this.containerTop = tempLocation[1];
        }

        private void adjust(View targetView, int limitY) {
            if (limitY < containerTop) {
                throw new IllegalArgumentException("limitY can not smaller than container top");
            }

            int offset = getScrollOffset(targetView, limitY);
            scrollContainerWithOffset(offset);
        }

        private void scrollContainerWithOffset(int offset) {

            //多偏移一定距离，使控件完全可见。
            final int finalOffset = insertPadding(offset);

            if (scrollAnimator == null) {
                scrollAnimator = new ValueAnimator();
                scrollAnimator.setDuration(SCROLL_DURATION);
            }

            if (scrollAnimator.isRunning()) {
                scrollAnimator.cancel();
            }

            scrollAnimator.removeAllUpdateListeners();

            tempScrollBy = 0;
            scrollAnimator.setIntValues(0, finalOffset);
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

        private static int insertPadding(int offset) {
            if (offset > 0) {
                return offset + 10;
            } else if (offset < 0) {
                return offset - 10;
            }
            return offset;
        }

        public void reset() {
            scrollContainerWithOffset(-containerScrollY);
        }

        private int getScrollOffset(View targetView, int limitY) {
            targetView.getLocationOnScreen(tempLocation);
            int targetTop = tempLocation[1];
            int targetBottom = tempLocation[1] + targetView.getHeight();
            if (targetTop >= containerTop && targetBottom <= limitY) {
                return 0;
            } else if (targetBottom > limitY) {
                return targetBottom - limitY;
            } else if (targetTop < containerTop) {
                return targetTop - containerTop;
            }
            return 0;
        }

    }

    private static class ViewRecycler implements ActivityCallbackHelper.LifecycleListener {

        @Override
        public void onLifecycle(Activity activity) {
            containers.remove(activity);
        }
    }

}
