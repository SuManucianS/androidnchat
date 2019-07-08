package com.example.chatapplication.services;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatapplication.R;
import com.example.chatapplication.dashBoard;
import com.example.chatapplication.shared.dialogSingleInput;
import com.example.chatapplication.system.registerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneSignInService {
    Boolean dialogFlag;
    String mVerificationId;
    FirebaseAuth mAuth;
    Context mContext;
    dialogSingleInput dsi;
    userService us;
    Activity mActivity;


    public phoneSignInService(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        us = new userService(context, activity);
        dsi = new dialogSingleInput(context);
        dialogFlag = false;
    }

    public void signInWithPhoneNumber() {
        if (!dialogFlag) {
            dialogFlag = false;
            LayoutInflater li = LayoutInflater.from(mContext);
            View view = li.inflate(R.layout.dialog_otp_password, null);
            Dialog dialog = dsi.openDialog(view);
            TextView tvtitle = (TextView) view.findViewById(R.id.tvOTPTitle);
            tvtitle.setText("Please Enter Your Phone Number");
            EditText ettext = (EditText) view.findViewById(R.id.etOPT);
            ettext.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_PHONE);
            ettext.setHint("Phone number");
            Button btncancel = (Button) view.findViewById(R.id.btnOPTCancel);
            Button btnok = (Button) view.findViewById(R.id.btnOTPConfirm);
            btncancel.setOnClickListener(v -> dialog.dismiss());
            btnok.setOnClickListener(v -> validateNumberAndSend(ettext, dialog));
            dialog.show();
        } else {
            LayoutInflater li = LayoutInflater.from(mContext);
            View view = li.inflate(R.layout.dialog_otp_password, null);
            Dialog dialog = dsi.openDialog(view);
            TextView tvtitle = (TextView) view.findViewById(R.id.tvOTPTitle);
            tvtitle.setText("Enter the OTP Number we sent");
            EditText ettext = (EditText) view.findViewById(R.id.etOPT);
            ettext.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_PHONE);
            ettext.setHint("OPT");
            Button btncancel = (Button) view.findViewById(R.id.btnOPTCancel);
            Button btnok = (Button) view.findViewById(R.id.btnOTPConfirm);
            btncancel.setOnClickListener(v -> restartField(dialog));
            btnok.setOnClickListener(v -> validateNumberAndSend(ettext, dialog));
            dialog.show();
        }
    }


    private void validateNumberAndSend(EditText et, Dialog dialog) {
        if (!dialogFlag) {
            if (et.length() != 9) {
                Toast.makeText(mContext, "Invalid Number", Toast.LENGTH_LONG).show();
            } else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + et.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        mCallbacks);
                Toast.makeText(mContext, "Please wait", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        } else {
            if (et.length() <= 0) {
                Toast.makeText(mContext, "Can not be empty", Toast.LENGTH_LONG).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, et.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }

        }
    }

    private void restartField(Dialog dialog) {
        dialogFlag = false;
        dialog.dismiss();
    }

    //verify phone number by code received
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            signInWithPhoneAuthCredential(credential);
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            dialogFlag = true;
            signInWithPhoneNumber();
//            mResendToken = token;

            // ...
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            us.registerToDBService(user, 1);
                            Intent intent = new Intent(mActivity, dashBoard.class);
                            intent.putExtra("id", user.getUid());
                            // create progress dialog to delay start
                            final ProgressDialog progress = new ProgressDialog(mContext);
                            progress.setTitle("Connecting");
                            progress.setMessage("Please wait while we connect to devices...");
                            progress.show();
                            Runnable progressRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    progress.cancel();
                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 5000);
                            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        mActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                                    } else {
                                        mActivity.startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(mContext, "failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
