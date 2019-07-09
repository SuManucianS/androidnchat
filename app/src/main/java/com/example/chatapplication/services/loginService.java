package com.example.chatapplication.services;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import com.example.chatapplication.dashBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;

public class loginService {
    Context mContext;
    Activity mActivity;
    FirebaseAuth mAuth;
    cryptoService cs;
    int result;

    public loginService(Context context, Activity activity) {
        mActivity = activity;
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        cs = new cryptoService();
        result = 0;
    }

    public int loginWithInfo(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, getHashedPassword(password))
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            Intent intent = new Intent(mActivity, dashBoard.class);
                            intent.putExtra("id", user.getUid());
                            // create progress dialog to delay start
                            final ProgressDialog progress = new ProgressDialog(mContext);
                            progress.setTitle("Connecting");
                            progress.setMessage("Please wait while we connect to devices...");
                            progress.show();
                            Runnable progressRunnable = () -> progress.cancel();
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 5000);
                            progress.setOnCancelListener(dialog -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    mActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                                } else {
                                    mActivity.startActivity(intent);
                                }
                            });
                            //succeed login
                        } else {
                            Toast.makeText(mContext, "Please active your account via email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(mContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
        return result;
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
