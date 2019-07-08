package com.example.chatapplication.system;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatapplication.R;

public class registerActivity extends Activity {
    Button btnregister, btncancel;
    EditText etfullname, etemail, etpassword, etconfirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.requestFeature(Window.FEATURE_NO_TITLE | Window.FEATURE_ACTIVITY_TRANSITIONS );
            w.setEnterTransition(new Explode());
            w.setExitTransition(new Slide());
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_register);
        getView();
        bindEvent();
    }

    private void getView() {
        btncancel = (Button) findViewById(R.id.btnRegisterCancel);
        btnregister = (Button) findViewById(R.id.btnRegisterRegister);
        etfullname = (EditText) findViewById(R.id.etRegisterFullname);
        etpassword = (EditText) findViewById(R.id.etRegisterPassword);
        etemail = (EditText) findViewById(R.id.etRegisterUsername);
        etconfirmpassword = (EditText) findViewById(R.id.etRegisterConfirmPassword);
    }

    private void bindEvent() {
        btncancel.setOnClickListener(v -> finish());
        btnregister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

    }
}
