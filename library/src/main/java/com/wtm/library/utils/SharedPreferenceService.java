package com.wtm.library.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceService {

	private Context context;

	public SharedPreferenceService(Context context) {
		this.context = context;
	}

	public boolean clearSharedPreference(String para) {
		boolean flag = false;

		SharedPreferences sharedPreferences = this.context
				.getSharedPreferences(para, Context.MODE_PRIVATE);

		flag = sharedPreferences.edit().clear().commit();

		return flag;
	}

	public boolean saveToSharedPreference(String para,
			HashMap<String, ?> hashMap) {
		boolean flag = false;

		SharedPreferences sharedPreferences = this.context
				.getSharedPreferences(para, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		for (Map.Entry<String, ?> entry : hashMap.entrySet()) {
			if (entry.getValue() == null) {
				editor.putString(entry.getKey().toString(), "");
			} else {
				editor.putString(entry.getKey().toString(), entry.getValue()
						.toString());
			}
		}
		flag = editor.commit();
		return flag;
	}

	public HashMap<String, ?> readFromSharedPreference(String para) {
		HashMap<String, ?> hashMap = null;
		SharedPreferences sharedPreferences = this.context
				.getSharedPreferences(para, Context.MODE_PRIVATE);
		hashMap = (HashMap<String, ?>) sharedPreferences.getAll();
		return hashMap;
	}

}
