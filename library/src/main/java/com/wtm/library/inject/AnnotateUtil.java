package com.wtm.library.inject;

import java.lang.reflect.Field;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.wtm.library.resources.ResLoader;
import com.wtm.library.resources.ResType;

public class AnnotateUtil {
	public static void initBindView(Object currentClass, View sourceView) {
		Field[] fields = currentClass.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				BindView bindView = field.getAnnotation(BindView.class);
				if (bindView != null) {
					int viewId = bindView.id();
					boolean clickLis = bindView.onClick();
					try {
						field.setAccessible(true);
						if (clickLis) {
							sourceView.findViewById(viewId).setOnClickListener(
									(OnClickListener) currentClass);
						}
						field.set(currentClass, sourceView.findViewById(viewId));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void initBindView(Activity aty) {
		initBindView(aty, aty.getWindow().getDecorView());
	}

	public static void initBindView(View view) {
		Context cxt = view.getContext();
		if (cxt instanceof Activity) {
			initBindView((Activity) cxt);
		} else {
			throw new RuntimeException("the view don't have root view");
		}
	}

	public static void initBindView(Fragment frag) {
		initBindView(frag, frag.getActivity().getWindow().getDecorView());
	}

	public static void initContentView(Activity aty) {
		if (aty.getClass().isAnnotationPresent(ContentView.class)) {
			ContentView contentView = (ContentView) aty.getClass()
					.getAnnotation(ContentView.class);
			int layoutID = contentView.layoutId();
			if (contentView != null && layoutID != 0) {
				aty.setContentView(layoutID);
			}
		}
	}

	public static void initResource(Object currentClass) {
		Field[] fields = currentClass.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				Resource resource = field.getAnnotation(Resource.class);
				if (resource != null) {
					try {
						int resId = resource.id();
                        ResType resType = resource.type();
						Object res = ResLoader.loadRes(resType, resId);
						field.setAccessible(true);
						field.set(currentClass, res);
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("DEBUG", AnnotateUtil.class.getName() + e);
					}
				}
			}
		}

	}
}
