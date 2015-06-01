package com.chanxa.wnb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;

import com.wtm.library.utils.SharedPreferenceService;

/**
 * 保存搜索的dao
 */
public class SearchRecordDataDAO{

	private static final String TAG = "SearchRecordData";
	private static final String KEY = "SearchRecordDataArr";
	private SharedPreferenceService sharedPreferenceService;

	public SearchRecordDataDAO(Context context) {
		sharedPreferenceService = new SharedPreferenceService(context);
	}

	public void add(Object object) {
		HashMap<String, String> allData = findAll();
		ArrayList<String> arrayList = findAll2List();
		if (arrayList == null) {
			arrayList = new ArrayList<String>();
		}
		if (arrayList.contains(object.toString())) {
			arrayList.remove(object.toString());
		}
		arrayList.add(object.toString());
		StringBuilder sb = new StringBuilder();
		for (String ss : arrayList) {
			sb.append(ss + ",");
		}
		allData.clear();
		allData.put(KEY, sb.toString());
		sharedPreferenceService.saveToSharedPreference(TAG, allData);
	}


	public void clear() {
		sharedPreferenceService.clearSharedPreference(TAG);
	}


	@SuppressWarnings("unchecked")
	public HashMap<String, String> findAll() {
		HashMap<String, String> localMap = (HashMap<String, String>) sharedPreferenceService
				.readFromSharedPreference(TAG);
		if (localMap == null || localMap.size() == 0) {
			localMap = new HashMap<String, String>();
			localMap.put(KEY, "");
			sharedPreferenceService.saveToSharedPreference(TAG, localMap);
			return localMap;
		}
		return (HashMap<String, String>) sharedPreferenceService
				.readFromSharedPreference(TAG);
	}

	public ArrayList<String> findAll2List() {
		String value = findAll().get(KEY).toString();
		if (!value.equals("")) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.addAll(Arrays.asList(value.split(",")));
			return temp;
		}
		return null;
	}
}
