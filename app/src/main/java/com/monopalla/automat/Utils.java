package com.monopalla.automat;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

public class Utils {
    public static SpannableString decorateText(String text, Drawable icon) {
        SpannableString textSpan = new SpannableString(text);
        icon.setBounds(0, 0, 55, 55);
        ImageSpan iconSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
        int startIndex = text.indexOf("%icon%");
        textSpan.setSpan(iconSpan, startIndex, startIndex + 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return textSpan;
    }
}
