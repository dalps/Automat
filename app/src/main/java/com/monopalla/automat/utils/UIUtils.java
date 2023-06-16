package com.monopalla.automat.utils;

import android.content.res.Resources;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class UIUtils {
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void showSnackbar(View view, String message, View anchor) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAnchorView(anchor)
                .show();
    }

    public static void showErrorSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(0xffff4444) // #ffcc0000
                .show();
    }

    public static void showErrorSnackbar(View view, String message, View anchor) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAnchorView(anchor)
                .setBackgroundTint(0xffff4444) // #ffcc0000
                .show();
    }
}
