package com.hua.keyboardmanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hua.softkeyboard_like.OnSoftKeyboardLikeDismissListener;
import com.hua.softkeyboard_like.SoftKeyboardLike;

public class MainActivity extends AppCompatActivity {

    static {
        SimpleCustomKeyboardLikeLayout.register();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EditText edt2 = findViewById(R.id.edt_2);
//        edt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("@@@hua","edt2 click");
//            }
//        });

        final EditText editText1 = findViewById(R.id.edit1);
        final EditText editText2 = findViewById(R.id.edit2);

        editText2.requestFocus();

        findViewById(R.id.btn_login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCIMSoftInputUtils.hideSoftInput(MainActivity.this, editText1);
                SoftKeyboardLike.show(v, 0, 200);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SoftKeyboardLike.isShowing()) {
                    SoftKeyboardLike.dismiss(new OnSoftKeyboardLikeDismissListener() {
                        @Override
                        public void onDismiss() {
                            LCIMSoftInputUtils.showSoftInput(MainActivity.this, editText2);
                        }
                    });
                } else {
                    LCIMSoftInputUtils.hideSoftInput(MainActivity.this, editText2);
                    SoftKeyboardLike.show(v, 0,200);
                }
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardLike.dismiss(new OnSoftKeyboardLikeDismissListener() {
                    @Override
                    public void onDismiss() {
                        LCIMSoftInputUtils.showSoftInput(MainActivity.this, editText2);
                    }
                });
            }
        });
    }

    public static class LCIMSoftInputUtils {

        /**
         * 如果当前键盘已经显示，则隐藏
         * 如果当前键盘未显示，则显示
         *
         * @param context
         */
        public static void toggleSoftInput(Context context) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        /**
         * 弹出键盘
         *
         * @param context
         * @param view
         */
        public static void showSoftInput(Context context, View view) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        }

        /**
         * 隐藏键盘
         *
         * @param context
         * @param view
         */
        public static void hideSoftInput(Context context, View view) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
