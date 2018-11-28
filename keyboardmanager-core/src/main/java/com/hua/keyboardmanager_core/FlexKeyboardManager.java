package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.thinkive.framework.keyboard.KeyboardManager;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 14:03
 */

public final class FlexKeyboardManager {

    private IKeyboardPanel keyboardPanel;
    private IKeyboardPanel tkKeyboardPanel;
    static SparseArray<IKeyboardTheme> customKeyboardThemes = new SparseArray<>();
    public static SparseArray<Short> themeIdTkKeyboardTypeMap = new SparseArray<>();
    private static final int DEFAULT_THEME_ID = R.id.keyboard_type_english;

    static {
        customKeyboardThemes.put(R.id.keyboard_type_custom_demo, new SimpleKeyboardTheme());
    }

    static {
        //FlexKeyboardManager处理的ThemeId映射为框架键盘类型。
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_english, KeyboardManager.KEYBOARD_TYPE_ENGLISH);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_stock, KeyboardManager.KEYBOARD_TYPE_STOCK);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_digital, KeyboardManager.KEYBOARD_TYPE_DIGITAL);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_random_digital, KeyboardManager.KEYBOARD_TYPE_RANDOM_DIGITAL);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_random_common, KeyboardManager.KEYBOARD_TYPE_RANDOM_COMMON);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_merchandise, KeyboardManager.KEYBOARD_TYPE_MERCHANDISE);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_common, KeyboardManager.KEYBOARD_TYPE_COMMON);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_third_board, KeyboardManager.KEYBOARD_TYPE_THIRD_BOARD);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_ios_digital, KeyboardManager.KEYBOARD_TYPE_IOS_DIGITAL);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_ios_digital_random, KeyboardManager.KEYBOARD_TYPE_IOS_DIGITAL_RANDOM);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_ios_alphabet, KeyboardManager.KEYBOARD_TYPE_IOS_ALPHABET);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_ios_sign_digital, KeyboardManager.KEYBOARD_TYPE_IOS_SIGN_DIGITAL);
        themeIdTkKeyboardTypeMap.put(R.id.keyboard_type_ios_sign, KeyboardManager.KEYBOARD_TYPE_IOS_SIGN);
    }

    private FlexKeyboardManager() {
        keyboardPanel = new KeyboardPanelImpl();
        tkKeyboardPanel = new TkKeyboardPanelImpl();
    }

    public static FlexKeyboardManager get() {
        return HOLDER.S_INSTANCE;
    }

    private static final class HOLDER {
        private static final FlexKeyboardManager S_INSTANCE = new FlexKeyboardManager();
    }

    public static void showSystemSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = activity.getWindow().getCurrentFocus();
            if (focusView != null) {
                imm.showSoftInput(focusView, 0);
            }
        }
    }

    public static void dismissSystemSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public void showCustomSoftInput(Activity activity) {
        this.showCustomSoftInput(activity, DEFAULT_THEME_ID, activity.getWindow().getCurrentFocus());
    }

    public void showCustomSoftInput(Activity activity,
                                    @IdRes int themeId) {
        this.showCustomSoftInput(activity, themeId, activity.getWindow().getCurrentFocus());
    }

    public void showCustomSoftInput(final Activity activity,
                                    @IdRes final int themeId,
                                    final View visibleView) {
        if (isSysKeyboardShowing(activity)) {
            dismissSystemSoftInput(activity);
        }
        if (tkKeyboardPanel.support(themeId)) {
            if (keyboardPanel.isShowing()) {
                keyboardPanel.dismiss();
            }
            tkKeyboardPanel.show(activity, themeId, visibleView);
        } else if (keyboardPanel.support(themeId)) {
            if (tkKeyboardPanel.isShowing()) {
                tkKeyboardPanel.dismiss();
            }
            keyboardPanel.show(activity, themeId, visibleView);
        }
    }

    public void dismissCustomSoftInput() {
        tkKeyboardPanel.dismiss();
        keyboardPanel.dismiss();
    }

    public static boolean isSysKeyboardShowing(Activity activity) {
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    public boolean isCustomShowing() {
        return tkKeyboardPanel.isShowing() || keyboardPanel.isShowing();
    }

    public static void registerKeyboardTheme(IKeyboardTheme theme) {
        customKeyboardThemes.put(theme.themeId(), theme);
    }
}
