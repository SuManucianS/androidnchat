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
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.services.registerService;

public class registerActivity extends Activity {
    Button btnregister, btncancel;
    EditText etfullname, etemail, etpassword, etconfirmpassword;
    registerService rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.requestFeature(Window.FEATURE_NO_TITLE | Window.FEATURE_ACTIVITY_TRANSITIONS);
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
        rs = new registerService(this, this);
    }

    private void bindEvent() {
        btncancel.setOnClickListener(v -> finish());
        btnregister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        if (etemail.getText().length() == 0 || etpassword.getText().length() == 0 || etconfirmpassword.getText().length() == 0 || etfullname.getText().length() == 0) {
            Toast.makeText(this, "Please don't leave a field blank", Toast.LENGTH_SHORT).show();
        } else {
            rs.validateService(etfullname.getText().toString().trim(), etemail.getText().toString().trim(), etpassword.getText().toString().trim(), etconfirmpassword.getText().toString().trim());
        }
    }
}
