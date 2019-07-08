package com.example.chatapplication.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatapplication.R;
import com.example.chatapplication.model.userModel;
import com.example.chatapplication.shared.dialogSingleInput;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class userService {
    Context mContext;
    userModel userModel;
    Activity mActivity;
    dialogSingleInput dsi;
    DatabaseReference databaseReference;

    public userService(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        dsi = new dialogSingleInput(context);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void registerToDBService(FirebaseUser user, int type) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("user/" + user.getUid())) {
                    return;
                } else {
                    switch (type) {
                        case 0:
                            userModel = new userModel(user.getUid(), user.getDisplayName(), "default", "online", "available");
                            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(userModel);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("calling","nocall");
                            hashMap.put("receiving", "nocall");
                            hashMap.put("id",user.getUid());
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("call").child(user.getUid()).setValue(hashMap);
                            break;
                        case 1:
                            userModel = new userModel(user.getUid(), "null", "default", "online", "available");
                            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(userModel);
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("calling","nocall");
                            hashMap1.put("receiving", "nocall");
                            hashMap1.put("id",user.getUid());
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                            ref1.child("call").child(user.getUid()).setValue(hashMap1);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void validateUser(String id) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("user/" + id)) {
                    userModel user = dataSnapshot.child("user/" + id).getValue(userModel.class);
                    if (user.getUsername().equals("null")) {
                        LayoutInflater li = LayoutInflater.from(mContext);
                        View view = li.inflate(R.layout.dialog_otp_password, null);
                        Dialog dialog = dsi.openDialog(view);
                        TextView tvtitle = (TextView) view.findViewById(R.id.tvOTPTitle);
                        tvtitle.setText("You don't have an Username yet, Please enter one");
                        TextView tvheader = (TextView) view.findViewById(R.id.tvOTPHeader);
                        tvheader.setVisibility(View.GONE);
                        EditText ettext = (EditText) view.findViewById(R.id.etOPT);
                        ettext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                        ettext.setHint("Username");
                        Button btncancel = (Button) view.findViewById(R.id.btnOPTCancel);
                        Button btnok = (Button) view.findViewById(R.id.btnOTPConfirm);
                        btncancel.setVisibility(View.GONE);
                        btnok.setOnClickListener(v -> addUsername(ettext, dialog, id));
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    } else{
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addUsername(EditText et, Dialog dialog, String id) {
        if (et.length() >= 6) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseDatabase.getInstance().getReference().child("user/" + id).child("username").setValue(et.getText().toString());
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(mContext, "username must be at least 6 characters long", Toast.LENGTH_LONG).show();
        }
    }
}
