package com.hua.keyboardmanager_core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/25
 */
public class SimpleKeyboardTheme implements IKeyboardTheme {
    private EditText inputTarget;

    @Override
    public int themeId() {
        return R.id.keyboard_theme_simple;
    }

    @Override
    public View onCreateKeyboardView(Context context, LayoutInflater inflater, ViewGroup container) {
        View inputView = inflater.inflate(R.layout.keyboard_simple_digital, container, false);
        GridView grid = inputView.findViewById(R.id.digital_grid);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(i + "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.item_keyboard_simple_digital, R.id.text, data);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inputTarget != null) {
                    int curCursorIndex = inputTarget.getSelectionStart();
                    inputTarget.getText().insert(curCursorIndex, position + "");
                }
            }
        });
        return inputView;
    }

    @Override
    public void onBindInputTarget(@NonNull View target) {
        this.inputTarget = (EditText) target;
    }

}
