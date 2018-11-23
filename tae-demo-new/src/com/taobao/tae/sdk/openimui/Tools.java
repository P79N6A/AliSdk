package com.taobao.tae.sdk.openimui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

public class Tools {
	public static void showToast(Handler mainHandler, final Context context, final String info){
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, info,
						Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public static String getEditText(Activity activity, int id) {
		EditText view = (EditText)activity.findViewById(id);
		return view.getText().toString();
	}
	
	private static String getStringWithNull(String text) {
		if(null != text && text.length() >0) {
			return text;
		}
		if(Constant.TEST_NULL_PARAM) {
			return null;
		}
		return "";
	}
	
	public static String getEditTextWithNull(EditText editText) {
		if(editText != null) {
			String text = editText.getText().toString().trim();
			return getStringWithNull(text);
		}
		return null;
	}
	
	public static String getEditTextWithNull(Activity activity, int id) {
		String text = getEditText(activity, id).trim();
		return getStringWithNull(text);
	}
	
	public static List<String> splitToList(String param) {
		if(param != null) {
			String[] keys = param.split(" ");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < keys.length; i++) {
				list.add(keys[i]);
			}
			return list;
		}
		return null;
	}
	
	public static List<String> splitToListWithNull(String param) {
		if(param != null && param.trim().length()>0) {
			return splitToList(param);
		}
		if(Constant.TEST_NULL_PARAM) {
			return null;
		}
		return new ArrayList<String>();
		
	}
}
