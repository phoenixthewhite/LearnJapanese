package com.vnshine.learnjapanese.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vnshine.learnjapanese.R;

/**
 * Created by phoenix on 12/6/17.
 */

public class CloseDialog extends Dialog {

    Button exit;
    Button cancel;

    public CloseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_close);
        initComponent();
    }

    private void initComponent() {
        exit = findViewById(R.id.exit_button);
        cancel = findViewById(R.id.cancel_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
