package com.ycjw.minesecurity.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AndroidUtil {
    //隐藏软键盘
    public static void hideOneInputMethod(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(act.getWindow().getDecorView().getWindowToken(), 0);
    }

    //打开软键盘
    public static void openInput(Activity act,View view){
        InputMethodManager imm = (InputMethodManager) act.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
}
