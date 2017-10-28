package com.vnshine.learnjapanese.CustomFont;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by phoenix on 27/10/17.
 */

public class TextView2 extends TextView {

    public TextView2(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);

    }

    public TextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("uvf2.ttf", context);
        setTypeface(customFont);
    }
}
