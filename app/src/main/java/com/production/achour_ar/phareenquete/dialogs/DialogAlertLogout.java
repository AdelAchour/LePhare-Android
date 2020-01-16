package com.production.achour_ar.phareenquete.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.production.achour_ar.phareenquete.R;
import com.production.achour_ar.phareenquete.activities.HomeAct;
import com.production.achour_ar.phareenquete.managers.Constants;

public class DialogAlertLogout {

    private Button closeBtn;
    private Button okBtn;

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_alert_logout);

        initView(dialog);
        setListener(dialog);

        dialog.show();
    }


    private void initView(Dialog dialog) {
        closeBtn = dialog.findViewById(R.id.closeBtn);
        okBtn = dialog.findViewById(R.id.okBtn);
    }


    private void setListener(final Dialog dialog) {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeAct.handler.sendEmptyMessage(Constants.LOGOUT);
                dialog.dismiss();
            }
        });
    }

}