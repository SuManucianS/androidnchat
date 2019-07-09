package com.example.chatapplication.services;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;

public class registerService {
    Context mContext;
    Activity mActivity;
    FirebaseAuth mAuth;
    userService us;
    cryptoService cs;
    int result;

    public registerService(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        us = new userService(context, activity);
        cs = new cryptoService();
        result = -1;
    }


    public void validateService(String fullname, String email, String password, String repassword) {
        if (fullname.length() < 6) {
            Toast.makeText(mContext, "Full name must be at least 6 character long", Toast.LENGTH_LONG).show();
        } else if (!password.equals(repassword)) {
            Toast.makeText(mContext, "Please make sure your passwords match", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, getHashedPassword(password))
                    .addOnCompleteListener(mActivity, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(mContext, "Register succeed, please check email to verify account", Toast.LENGTH_SHORT).show();
                                            us.registerToDBService(user, 2, fullname);
                                            mActivity.finish();
                                        } else {
                                            Toast.makeText(mContext, task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        // ...
                    });
        }
    }

    public String getHashedPassword(String pass) {
        String hash = null;
        try {
            hash = cs.getSHA256Hash(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

}
