package com.vnshine.learnjapanese.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.vnshine.learnjapanese.R;

/**
 * Created by phoenix on 12/8/17.
 */

public class ResultDialog extends Dialog {
    Context context;

    public ResultDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_test_result);
    }
}
