package com.hua.keyboardmanager_core;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;

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
    private static HashMap<Activity, List<View>> targets;
    private static HashMap<View, ScrollTarget> scrollTargets;
    private static ViewRecycler viewRecycler = new ViewRecycler();

    public static int getViewTopOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    public static void adjust(View targetView, int limitY) {
        if (targets == null) {
            targets = new HashMap<>();
        }
        if (scrollTargets == null) {
            scrollTargets = new HashMap<>();
        }

        Context context = targetView.getContext();
        if (context instanceof Activity) {
            List<View> views = targets.get(context);
            if (views != null) {
                views.add(targetView);
            } else {
                views = new ArrayList<>();
                views.add(targetView);
                targets.put((Activity) context, views);
                ActivityCallbackHelper.doOnActivityDestroyed((Activity) context, viewRecycler);
            }
        } else {
            throw new IllegalArgumentException("context of " + targetView + " must be Activity!!!");
        }

        ScrollTarget scrollTarget = scrollTargets.get(targetView);
        if (scrollTarget != null) {
            scrollTarget.update(targetView, limitY);
        } else {
            scrollTarget = new ScrollTarget(targetView, limitY);
            scrollTargets.put(targetView, scrollTarget);
        }

        scrollTarget.adjust();
    }

    public static void reset(View targetView) {
        if (scrollTargets != null) {
            ScrollTarget scrollTarget = scrollTargets.get(targetView);
            if (scrollTarget != null) {
                scrollTarget.reset();
            }
        }
    }

    private static class ScrollTarget {
        private View targetView;
        private View containerView;
        private int limitY;
        private ValueAnimator scrollAnimator;
        private int containerScrollY;
        private int tempScrollBy;

        private ScrollTarget(View targetView, int limitY) {
            this.targetView = targetView;
            this.limitY = limitY;
            this.containerScrollY = 0;
        }

        private void update(View targetView, int limitY) {
            this.targetView = targetView;
            this.limitY = limitY;
            this.containerScrollY = 0;
        }

        private void adjust() {
            int offset = getScrollOffset(targetView, limitY);
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

        private static int getScrollOffset(View targetView, int limitY) {
            int[] tempLocation = new int[2];
            targetView.getLocationOnScreen(tempLocation);
            int focusBottom = tempLocation[1] + targetView.getHeight();
            int offset = focusBottom - limitY;
            return offset > 0 ? offset : 0;
        }

    }

    private static class ViewRecycler implements ActivityCallbackHelper.LifecycleListener {

        @Override
        public void onLifecycle(Activity activity) {
            List<View> views = targets.remove(activity);
            if (views != null) {
                for (View view : views) {
                    scrollTargets.remove(view);
                }
            }
        }
    }

}
