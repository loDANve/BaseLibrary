package com.chanxa.wnb.androidService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.chanxa.wnb.R;
import com.chanxa.wnb.config.AppConfig;
import com.chanxa.wnb.config.WnbApplication;
import com.wtm.library.http.HttpAsynCallBack;
import com.wtm.library.http.HttpResponseCallBack;
import com.wtm.library.http.HttpTask;
import com.wtm.library.http.ThreadPoolManager;
import com.wtm.library.http.download.DownloadProgressListener;
import com.wtm.library.http.download.FileDownloader;
import com.wtm.library.inject.ContentView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpDataDownLoadService extends Service {

    private Notification notification;
    private NotificationCompat.Builder notificationBuilder;

    private NotificationManager notificationManager = null;
    int notification_id = 19172439;


    public UpDataDownLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.e("onCreate");
        super.onCreate();
        initNotification();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
       LogUtils.e("onStart");
        if (intent == null) {
            return;
        }
        final String downLoadUrl = intent.getStringExtra(AppConfig.DOWNLOAD_URL_KEY);
        if (StringUtils.isEmpty(downLoadUrl) || WnbApplication.getInstance().isDownLoad()) {
            return;
        }
        final String savePath = FileUtils.getSDPATH() + "wnb.apk";

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                WnbApplication.getInstance().setDownLoad(true);
                try {
                    File file = new File(savePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    URL url = new URL(downLoadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    inputStream = conn.getInputStream();
                    int count = conn.getContentLength();
                    int size = 0;
                    fileOutputStream = new FileOutputStream(file);
                    int len = 0;
                    byte[] buff = new byte[1024];
                    int number = 5;
                    long curTime = System.currentTimeMillis();
                    while ((len = (inputStream.read(buff))) != -1) {
                        fileOutputStream.write(buff, 0, len);
                        size += len;
                        if (System.currentTimeMillis() - curTime > 2000) {
                            upDataNotifacation(count,size);
                            curTime = System.currentTimeMillis();
                        }
                        LogUtils.e("count:" + count + "||" + "size:" + size);
                    }
                    LogUtils.e("下载成功");
                    //SystemTool.installApk(UpDataDownLoadService.this,file);
                    Intent intent = new Intent();
                    intent.setAction(AppConfig.DOWNLOAD_RECEIVERC);
                    intent.putExtra("file", file);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    ViewInject.toast("下载更新失败");
                } finally {
                    notificationManager.cancel(notification_id);
                    WnbApplication.getInstance().setDownLoad(false);
                    FileUtils.closeIO(inputStream, fileOutputStream);
                }
            }
        }).start();
    }

    public void initNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.wnb_big, "在下载新版本", System.currentTimeMillis());
        notification.contentView = new RemoteViews(getPackageName(), R.layout.notification);
        notification.contentView.setProgressBar(R.id.pb, 100, 0, true);
        notificationManager.notify(notification_id, notification);
    }

    public void upDataNotifacation(int max, int current) {
        if (null == notification) {
            initNotification();
        }
        RemoteViews contentview = notification.contentView;
        contentview.setProgressBar(R.id.pb, max, current, false);
        contentview.setTextViewTextSize(R.id.down_tv, TypedValue.COMPLEX_UNIT_SP,12);
        contentview.setTextViewText(R.id.down_tv, "正在下载 " +((int)((double)current/(double)max*100))+"%");
        notificationManager.notify(notification_id, notification);
    }

    public void upDataError(){
        RemoteViews contentview = notification.contentView;
        contentview.setProgressBar(R.id.pb, 0, 0, false);
        contentview.setTextViewText(R.id.down_tv, "下载失败");
        notificationManager.notify(notification_id, notification);
    }

}