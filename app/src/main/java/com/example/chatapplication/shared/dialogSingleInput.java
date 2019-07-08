package com.example.chatapplication.shared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chatapplication.R;
import com.example.chatapplication.interfaces.OnDialogButtonClickListener;

public class dialogSingleInput {
    Context mContext;
    public EditText result;

    public dialogSingleInput(Context context) {
        mContext = context;
    }

    public void openAlertDialog(Context context, String message, String positiveBtnText, String negativeBtnText,
                                final OnDialogButtonClickListener listener) {
//        LayoutInflater li = LayoutInflater.from(context);
//        View promptsView = li.inflate(R.layout.dialog_otp_password, null);
//        result = (EditText) promptsView.findViewById(R.id.etOPT);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setView(promptsView);
//        }
        if (positiveBtnText != null) {
            builder.setPositiveButton(positiveBtnText, (dialog, which) -> {
                listener.onPositiveButtonClicked();
                dialog.dismiss();
            });
        }
        if (negativeBtnText != null) {
            builder.setNegativeButton(negativeBtnText, (dialog, which) -> {
                listener.onNegativeButtonClicked();
                dialog.dismiss();
            });
        }
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setCancelable(false);
        builder.create().show();
    }

    public Dialog openDialog(View view) {
        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
