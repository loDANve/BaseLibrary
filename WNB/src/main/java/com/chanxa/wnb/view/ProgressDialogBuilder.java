package com.chanxa.wnb.view;

import android.app.ProgressDialog;
import android.content.Context;

import com.wtm.library.utils.DeviceUtils;

/**
 * Created by CHANXA on 2014/12/29.
 */
public class ProgressDialogBuilder {

    public static ProgressDialog builderDialog(Context context){
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(context);
        progressDialog.setMessage("请稍候");
        progressDialog.getWindow().setLayout(DeviceUtils.getScreenW((android.app.Activity) context),
                DeviceUtils.getScreenH((android.app.Activity) context));
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }
}
