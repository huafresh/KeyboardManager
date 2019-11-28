package com.hua.keyboardmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hua.softkeyboard_core.LikeSoftKeyboard;

public class MainActivity extends AppCompatActivity {

    static {
        SimpleCustomKeyboard.register();
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

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeSoftKeyboard.show(MainActivity.this, v, 0);
            }
        });
    }
}
