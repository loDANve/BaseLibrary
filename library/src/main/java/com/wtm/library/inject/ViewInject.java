package com.wtm.library.inject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wtm.library.base.WActivityManager;
import com.wtm.library.resources.ResLoader;
import com.wtm.library.resources.ResType;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.StringUtils;
import com.wtm.library.utils.SystemTool;
import com.wtm.mylibrary.R;


public class ViewInject {

	private ViewInject() {
	}

	private static volatile ViewInject instance;
	private static Toast toast;

	public static ViewInject getInstance() {
		if (null == instance) {
			synchronized (ViewInject.class) {
				if (null == instance) {
					instance = new ViewInject();
				}
			}
		}
		return instance;
	}

	public static void toast(int msg) {
		try {
			toast(WActivityManager.getInstance().topActivity(), "" + msg);
		} catch (Exception e) {
		}
	}

    public static void toast(String msg) {
        try {
            toast(WActivityManager.getInstance().topActivity(), msg);
        } catch (Exception e) {
        }
    }

    public static void toast(Object object) {
        toast(object.toString());
    }

	public static void longToast(String msg) {
		try {
			longToast(WActivityManager.getInstance().topActivity(), msg);
		} catch (Exception e) {
		}
	}

	public static void longToast(int msg) {
        longToast(msg+"");
	}

    public static void longToast(Object object) {
        longToast(object.toString());
    }

	public static void longToast(Context context, String msg) {
		
		if (toast==null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		}else {
			toast.setText(msg);
		}
		toast.show();
	}

	public static void toast(Context context, String msg) {
		if (toast==null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}else {
			toast.setText(msg);
		}
		toast.show();
	}

	public void getExitDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String exitMsg = (String) ResLoader.loadRes(ResType.String,
				R.string.exitSentence);
		String okStr = (String) ResLoader.loadRes(ResType.String, R.string.ok);
		String cancleStr = (String) ResLoader.loadRes(ResType.String,
				R.string.cancel);
		builder.setMessage(exitMsg);
		builder.setCancelable(false);
		builder.setNegativeButton(cancleStr, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(okStr, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				WActivityManager.getInstance().appExit(context);
			}
		});
		builder.create();
		builder.show();
		builder = null;
	}

	public PopupWindow createWindow(View view, int w, int h, int argb) {
		PopupWindow popupView = new PopupWindow(view, w, h);
		popupView.setFocusable(true);
		popupView.setBackgroundDrawable(new ColorDrawable(argb));
		popupView.setOutsideTouchable(true);
		return popupView;
	}

	public void getDateDialog(String title, final TextView textView) {
		final String[] time = SystemTool.getDataTime("yyyy-MM-dd").split("-");
		final int year = StringUtils.toInt(time[0], 0);
		final int month = StringUtils.toInt(time[1], 1);
		final int day = StringUtils.toInt(time[2], 0);
		DatePickerDialog dialog = new DatePickerDialog(textView.getContext(),
				new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						textView.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);
					}
				}, year, month - 1, day);
		dialog.setTitle(title);
		dialog.show();
	}

	public static ProgressDialog getprogress(Activity aty, String msg,
			boolean cancel) {
		ProgressDialog progressDialog = new ProgressDialog(aty);
		progressDialog.setMessage(msg);
		progressDialog.getWindow().setLayout(DeviceUtils.getScreenW(aty),
				DeviceUtils.getScreenH(aty));
		progressDialog.setCancelable(cancel);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return progressDialog;
	}
}
