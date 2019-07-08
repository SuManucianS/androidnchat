package com.example.chatapplication.system;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chatapplication.R;
import com.example.chatapplication.services.googleSignInService;
import com.example.chatapplication.services.phoneSignInService;
import com.google.android.gms.common.SignInButton;


public class loginActivity extends Activity {
    Boolean flag;
    EditText etusername, etpassword;
    Button btnsignin, btnothersignin, btnphonenumber;
    TextView tvforgot, tvregister;
    SignInButton ggSignInButton;
    googleSignInService gsis;
    phoneSignInService psis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.requestFeature(Window.FEATURE_NO_TITLE);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_login);
        getView();
        bindEvent();
    }

    private void getView() {
        flag = false;
        etusername = (EditText) findViewById(R.id.etLoginUsername);
        etpassword = (EditText) findViewById(R.id.etLoginPassword);
        btnsignin = (Button) findViewById(R.id.btnLoginSignIn);
        btnothersignin = (Button) findViewById(R.id.btnLoginOtherMethod);
        btnphonenumber = (Button) findViewById(R.id.btnLoginPhoneNumber);
        tvforgot = (TextView) findViewById(R.id.tvLoginForgot);
        tvregister = (TextView) findViewById(R.id.tvLoginRegister);
        ggSignInButton = (SignInButton) findViewById(R.id.btnGoogleButton);
        gsis = new googleSignInService(this, getString(R.string.default_web_client_id), this);
        psis = new phoneSignInService(this, this);
    }

    private void bindEvent() {
        btnsignin.setOnClickListener(v -> singin());
        btnothersignin.setOnClickListener(v -> changeView());
        btnphonenumber.setOnClickListener(v -> phoneSignIn());
        tvregister.setOnClickListener(v -> toRegister());
        ggSignInButton.setOnClickListener(v -> ggSignIn());
    }

    private void changeView() {
        etusername.setVisibility(etusername.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        etpassword.setVisibility(etpassword.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        flag = flag == false ? true : false;
        btnsignin.setText(btnsignin.getText().equals("sign in") ? "back" : "sign in");
        btnothersignin.setVisibility(btnothersignin.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        btnphonenumber.setVisibility(btnphonenumber.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        ggSignInButton.setVisibility(ggSignInButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void singin() {
        if (flag == false) {
            Toast.makeText(this, "This is a Sign in Button", Toast.LENGTH_LONG).show();
        } else {
            changeView();
        }
    }

    private void ggSignIn() {
        Intent intent = new Intent(gsis.mGoogleSignInClient.getSignInIntent());
        startActivityForResult(intent, 101);
    }

    private void phoneSignIn() {
        psis.signInWithPhoneNumber();
    }

    private void toRegister() {
        Intent intent = new Intent(this, registerActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gsis.handleResult(requestCode, resultCode, data);
    }

}
