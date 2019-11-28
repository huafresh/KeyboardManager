package com.hua.keyboardmanager_core;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.lang.reflect.Method;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/23 13:40
 */

public class KeyboardEditText extends AppCompatEditText
        implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener {
    public static final int KEYBOARD_TYPE_SYSTEM = 0;
    public static final int KEYBOARD_TYPE_CUSTOM = 1;
    private int keyboardType = KEYBOARD_TYPE_CUSTOM;
    private int keyboardThemeId;
    private int visibleViewId;
    private boolean systemSoftEnable = true;
    private OnKeyListener keyListener;
    private OnFocusChangeListener focusChangeListener;
    private OnClickListener clickListener;

    public KeyboardEditText(Context context) {
        this(context, null);
    }

    public KeyboardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public KeyboardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KeyboardEditText);
        this.keyboardType = ta.getInt(R.styleable.KeyboardEditText_keyboard_type, KEYBOARD_TYPE_CUSTOM);
        this.keyboardThemeId = ta.getResourceId(
                R.styleable.KeyboardEditText_keyboard_theme_id, R.id.keyboard_type_english);
        this.visibleViewId = ta.getResourceId(R.styleable.KeyboardEditText_keyboard_visible_view, 0);
        ta.recycle();

        setOnFocusChangeListener(this);
        setOnClickListener(this);
        setOnKeyListener(this);

        if (keyboardType == KEYBOARD_TYPE_CUSTOM && isActivityContext()) {
            setIsShowSystemSoftInputOnFocus(false);
        } else {
            setIsShowSystemSoftInputOnFocus(true);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (!(l instanceof KeyboardEditText)) {
            this.clickListener = l;
        } else {
            super.setOnClickListener(l);
        }
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (!(l instanceof KeyboardEditText)) {
            this.focusChangeListener = l;
        } else {
            super.setOnFocusChangeListener(l);
        }
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        if (!(l instanceof KeyboardEditText)) {
            this.keyListener = l;
        } else {
            super.setOnKeyListener(l);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //焦点变化快于系统键盘的弹出
        if (hasFocus) {
            if (!systemSoftEnable) {
                Activity activity = (Activity) getContext();
                View visibleView = activity.getWindow().getDecorView().findViewById(visibleViewId);
                if (visibleView == null) {
                    visibleView = this;
                }
                FlexKeyboardManager.get().showCustomSoftInput(activity, keyboardThemeId, visibleView);
            } else {
                FlexKeyboardManager.get().dismissCustomSoftInput();
            }
        }

        if (this.focusChangeListener != null) {
            focusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    private boolean isActivityContext() {
        return getContext() instanceof Activity;
    }

    private void setIsShowSystemSoftInputOnFocus(boolean show) {
        if (systemSoftEnable != show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setShowSoftInputOnFocus(show);
            } else {
                try {
                    Method setShowSoftInputOnFocus = EditText.class.getMethod(
                            "setShowSoftInputOnFocus", Boolean.TYPE);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(this, show);
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }
            systemSoftEnable = show;
        }
    }


    @Override
    public void onClick(View v) {
        //点击事件慢于系统键盘的弹出
        //如果点击使View获取了焦点，则此回调不会走。
        if (!systemSoftEnable &&
                hasFocus()) {
            Activity activity = (Activity) getContext();
            View visibleView = activity.getWindow().getDecorView().findViewById(visibleViewId);
            if (visibleView == null) {
                visibleView = this;
            }
            FlexKeyboardManager.get().showCustomSoftInput(activity, keyboardThemeId, visibleView);
        }

        if (this.clickListener != null) {
            clickListener.onClick(v);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                FlexKeyboardManager.get().isCustomShowing()) {
            FlexKeyboardManager.get().dismissCustomSoftInput();
            return true;
        }
        return keyListener != null && keyListener.onKey(v, keyCode, event);
    }
}
