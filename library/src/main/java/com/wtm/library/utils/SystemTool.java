package com.wtm.library.utils;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class SystemTool {
	/**
	 * ָ����ʽ���ص�ǰϵͳʱ��
	 */
	public static String getDataTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}

	/**
	 * ���ص�ǰϵͳʱ��(��ʽ��HH:mm��ʽ)
	 */
	public static String getDataTime() {
		return getDataTime("HH:mm");
	}

	/**
	 * ��ȡ�ֻ�IMEI��
	 */
	public static String getPhoneIMEI(Activity aty) {
		TelephonyManager tm = (TelephonyManager) aty
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * ��ȡ�ֻ�ϵͳSDK�汾
	 * 
	 * @return ��API 17 �򷵻� 17
	 */
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * ��ȡϵͳ�汾
	 * 
	 * @return ����2.3.3
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * ����ϵͳ���Ͷ���
	 */
	public static void sendSMS(Context cxt, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		cxt.startActivity(intent);
	}

	/**
	 * �ж������Ƿ�����
	 */
	public static boolean isCheckNet(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// �����Ƿ�����
    }

	/**
	 * �ж��Ƿ�Ϊwifi����
	 */
	public static boolean isWiFi(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi��״̬��ConnectivityManager.TYPE_WIFI
		// 3G��״̬��ConnectivityManager.TYPE_MOBILE
		State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		return State.CONNECTED == state;
	}

	/**
	 * ����ϵͳ����
	 * 
	 * @warn ������ȷ��������ʾʱ���ܵ���
	 */
	public static void hideKeyBoard(Activity aty) {
		((InputMethodManager) aty
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(
						aty.getCurrentFocus().getWindowToken(), 0);
	}

	/**
	 * �жϵ�ǰӦ�ó����Ƿ��̨����
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					// ��̨����
					return true;
				} else {
					// ǰ̨����
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * �ж��ֻ��Ƿ���˯��
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager kgMgr = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
		return isSleeping;
	}

	/**
	 * ��װapk
	 *
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * ��ȡ��ǰӦ�ó���İ汾��
	 */
	public static String getAppVersion(Context context) {
		String version = "0";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(SystemTool.class.getName()
					+ "the application not found");
		}
		return version;
	}

	/**
	 * �ص�home����̨����
	 */
	public static void goHome(Context context) {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}

	/**
	 * ��ȡӦ��ǩ��
	 * 
	 * @param context
	 * @param pkgName
	 */
	public static String getSign(Context context, String pkgName) {
		try {
			PackageInfo pis = context.getPackageManager().getPackageInfo(
					pkgName, PackageManager.GET_SIGNATURES);
			return hexdigest(pis.signatures[0].toByteArray());
		} catch (NameNotFoundException e) {
			throw new RuntimeException(SystemTool.class.getName() + "the "
					+ pkgName + "'s application not found");
		}
	}

	/**
	 * ��ǩ���ַ���ת������Ҫ��32λǩ��
	 */
	private static String hexdigest(byte[] paramArrayOfByte) {
		final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
				98, 99, 100, 101, 102 };
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramArrayOfByte);
			byte[] arrayOfByte = localMessageDigest.digest();
			char[] arrayOfChar = new char[32];
			for (int i = 0, j = 0;; i++, j++) {
				if (i >= 16) {
					return new String(arrayOfChar);
				}
				int k = arrayOfByte[i];
				arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
				arrayOfChar[++j] = hexDigits[(k & 0xF)];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��ȡ�豸�Ŀ����ڴ��С
	 * 
	 * @param cxt
	 *            Ӧ�������Ķ���context
	 * @return ��ǰ�ڴ��С
	 */
	public static int getDeviceUsableMemory(Context cxt) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// ���ص�ǰϵͳ�Ŀ����ڴ�
		return (int) (mi.availMem / (1024 * 1024));
	}

	/**
	 * �����̨���������
	 * 
	 * @param cxt
	 *            Ӧ�������Ķ���context
	 * @return �����������
	 */
	public static int gc(Context cxt) {
		long i = getDeviceUsableMemory(cxt);
		int count = 0; // ������Ľ�����
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		// ��ȡ�������е�service�б�
		List<RunningServiceInfo> serviceList = am.getRunningServices(100);
		if (serviceList != null)
			for (RunningServiceInfo service : serviceList) {
				if (service.pid == android.os.Process.myPid())
					continue;
				try {
					android.os.Process.killProcess(service.pid);
					count++;
				} catch (Exception e) {
					e.getStackTrace();
					continue;
				}
			}

		// ��ȡ�������еĽ����б�
		List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
		if (processList != null)
			for (RunningAppProcessInfo process : processList) {
				// һ����ֵ����RunningAppProcessInfo.IMPORTANCE_SERVICE�Ľ��̶���ʱ��û�û��߿ս�����
				// һ����ֵ����RunningAppProcessInfo.IMPORTANCE_VISIBLE�Ľ��̶��Ƿǿɼ����̣�Ҳ�����ں�̨������
				if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					// pkgList �õ��ý��������еİ���
					String[] pkgList = process.pkgList;
					for (String pkgName : pkgList) {
						Log.d("DEBUG", "======����ɱ��������" + pkgName);
						try {
							am.killBackgroundProcesses(pkgName);
							count++;
						} catch (Exception e) { // ��ֹ���ⷢ��
							e.getStackTrace();
							continue;
						}
					}
				}
			}
		Log.d("DEBUG", "������" + (getDeviceUsableMemory(cxt) - i) + "M�ڴ�");
		return count;
	}
}