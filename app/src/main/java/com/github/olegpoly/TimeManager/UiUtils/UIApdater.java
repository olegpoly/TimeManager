package com.github.olegpoly.TimeManager.UiUtils;

import android.app.Activity;
import android.widget.TextView;

/**
 * Used for updating a textView on an activity from different class/activity
 */
public class UIApdater {
    private Activity activity;
    private TextView view;

    public UIApdater(Activity activity, TextView view) {
        this.activity = activity;
        this.view = view;
    }

    public void setTextViewText(final String text) {
        if (text == null) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }
}
