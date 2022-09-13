package com.lhl.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

class Warning {
    private static int statusBarHeight = -1;

    Warning(Context context, Window window) {
        ViewGroup group = (ViewGroup) window.getDecorView();
        View warning = LayoutInflater.from(context).inflate(com.relax.playgame.gamelib.R.layout.warning, null);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Resources.getSystem().getDisplayMetrics().density * 30 + 0.5f));
        if ((window.getDecorView().getSystemUiVisibility() & View.INVISIBLE) != View.INVISIBLE) {
            if (statusBarHeight <= 0) {
                Resources resources = Resources.getSystem();
                int resourceId = resources.getIdentifier(
                        "status_bar_height", "dimen", "android");
                statusBarHeight = resources
                        .getDimensionPixelSize(resourceId);
            }
            params.topMargin = statusBarHeight;
        }
        group.addView(warning, params);

    }
}

