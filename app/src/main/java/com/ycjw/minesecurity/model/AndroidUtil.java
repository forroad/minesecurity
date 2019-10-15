package com.ycjw.minesecurity.model;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtil {
    public static void hideOneInputMethod(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(act.getWindow().getDecorView().getWindowToken(), 0);
    }
}
