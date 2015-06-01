package com.wtm.library.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.SystemTool;

public class UncaughtHandler implements UncaughtExceptionHandler {

    private Context mContext;
    public boolean openUpload = true;
    private static final String FILE_SUFFIX_NAME = ".log";

    private volatile static UncaughtHandler uncaughtHandler;
    private OnErrorUncaught onErrorUncaught;
    private UncaughtExceptionHandler mDefaultCrashHandler;

    private UncaughtHandler(Context context) {
        mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public final static UncaughtHandler getInstance(Context context) {
        if (uncaughtHandler == null) {
            synchronized (UncaughtHandler.class) {
                if (uncaughtHandler == null) {
                    uncaughtHandler = new UncaughtHandler(context);
                }
            }
        }
        return uncaughtHandler;
    }

    /**
     * ���������ﴦ��
     */
    @Override
    public final void uncaughtException(Thread thread, Throwable ex) {
        try {
            // �����쳣��Ϣ��SD����
            saveToSDCard(ex);
            // ͨ�������ϴ��쳣��Ϣ
            if (openUpload && SystemTool.isCheckNet(mContext)) {
                uploadLog();
            }
        } catch (IOException e) {//������,����ϵͳ�Լ�ȥ����
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } finally {//����˳�����
            //WActivityManager.getInstance().appExit(mContext);
            ex.printStackTrace();
            if (onErrorUncaught != null) {
                onErrorUncaught.action(ex);
            } else {
                WActivityManager.getInstance().appExit(mContext);
            }
        }
    }

    private final void saveToSDCard(Throwable ex) throws IOException {
        File fileDir = FileUtils.createSDDir("wAndroidLog");
        File file = new File(fileDir, SystemTool.getDataTime("yyyyMMdd") + System.currentTimeMillis() + FILE_SUFFIX_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(
                    file)));
            // ���������쳣��ʱ��
            pw.println(SystemTool.getDataTime("yyyyMMdd_HH_mm"));
            // �����ֻ���Ϣ
            dumpPhoneInfo(pw);
            pw.println();
            // �����쳣�ĵ���ջ��Ϣ
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(pw);
        }
    }

    private final void dumpPhoneInfo(PrintWriter pw)
            throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }

    public OnErrorUncaught getOnErrorUncaught() {
        return onErrorUncaught;
    }

    public void setOnErrorUncaught(OnErrorUncaught onErrorUncaught) {
        this.onErrorUncaught = onErrorUncaught;
    }

    public void setOpenUpload(boolean openUpload) {
        this.openUpload = openUpload;
    }

    /**
     * �ϴ���������,��������д
     */
    protected void uploadLog() {

    }

    public interface OnErrorUncaught {
        public void action(Throwable e);
    }

}
