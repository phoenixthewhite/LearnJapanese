package com.vnshine.learnjapanese.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.vnshine.learnjapanese.R;

/**
 * Created by phoenix on 12/12/17.
 */

public class CompleteDialog extends Dialog {
    Context context;
    public CompleteDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_complete);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
