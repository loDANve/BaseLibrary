package com.chanxa.wnb.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

/**
 * Created by CHANXA on 2014/12/31.
 */
public class DownLoadBoradcast  extends BroadcastReceiver {

    private OnDownLoadEnd onDownLoadEnd;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onDownLoadEnd != null) {
            onDownLoadEnd.onEnd((File) intent.getExtras().get("file"));
        }
    }

    public interface OnDownLoadEnd{
        void onEnd(File file);
    }

    public OnDownLoadEnd getOnDownLoadEnd() {
        return onDownLoadEnd;
    }

    public void setOnDownLoadEnd(OnDownLoadEnd onDownLoadEnd) {
        this.onDownLoadEnd = onDownLoadEnd;
    }
}
