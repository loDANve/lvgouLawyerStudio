package com.lvgou.lawyer.studio.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;

/**
 * 工具类
 * 
 * @author xiangyu
 * 
 */
public class Utils {

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 用share preference来实现是否绑定的开关。在onBind且成功时设置true，unBind且成功时设置false
	 * 
	 * @param context
	 * @param type
	 *            1:应用与百度云绑定状况 2:百度云推ID与用户ID绑定状态
	 * @return
	 */
	public static boolean hasBind(Context context) {
		SharedPreferences sp = getSharedPreferences(context,
				Globals.SPID_LVGOU_USERS);
		String flag = "";
		flag = sp.getString(Globals.SP_BINDBAIDU_FLAG, "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

	/**
	 * 保持百度云绑定状态
	 * 
	 * @param context
	 * @param flag
	 * @param type
	 */
	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put(Globals.SP_BINDBAIDU_FLAG, flagStr);
		editSharedPreferences(context, map, Globals.SPID_LVGOU_USERS);

	}

	/**
	 * 获取SharedPreferences文件对象
	 * 
	 * @param context
	 * @param spid
	 *            SharedPreferences文件ID
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context,
			String spid) {
		SharedPreferences spf = null;
		try {
			spf = context.getSharedPreferences(spid, Context.MODE_PRIVATE);
		} catch (NullPointerException e) {
			// e.printStackTrace();
			return null;
		}
		return spf;
	}

	/**
	 * 修改SharedPreferences文件
	 * 
	 * @param context
	 *            上下文
	 * @param map
	 *            需要修改的map
	 * @param spid
	 *            SharedPreferences文件ID
	 */
	public static void editSharedPreferences(Context context,
			Map<String, String> map, String spid) {
		if (null != map && null != context) {
			Editor editor = getSharedPreferences(context, spid).edit();
			for (String key : map.keySet()) {
				editor.putString(key, map.get(key));
			}
			editor.commit();
		}

	}

	/**
	 * 获取网络连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getNetworkState(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取手机mac地址
	 * 
	 * @param activity
	 * @return
	 */
	public static String getMacAddress(Activity activity) {
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		if (null != wifiManager) {
			WifiInfo wifiinfo = wifiManager.getConnectionInfo();
			String mac = wifiinfo.getMacAddress();
			if (null != mac)
				return mac.replace(":", "");
			else
				return null;
		} else {
			return null;
		}
	}

	public static <T> T json2Object(byte[] json, T t) {
		Gson gson = new Gson();
		T object = null;
		String str = null;
		try {
			str = new String(json, "utf-8");
			if (null != t) {
				LogUtils.e(t.getClass().toString() + " : " + str);
			}
			object = (T) gson.fromJson(str, t.getClass());
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return object;
	}

	// 接口加密
	public static String encrypt(String[] strs) {
		Arrays.sort(strs); // 序列化需加密参数值
		StringBuffer buffer = new StringBuffer();
		for (String str : strs) {
			buffer.append(str);
			System.out.println(str);
		}
		buffer.append(Globals.SECRETKEY); // 拼装加密字符串
		return getMD5Str(buffer.toString()); // 返回经过MD5加密的字符串
	}

	// MD5加密
	private static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		// 16位加密，从第9位到25位
		System.out.println(md5StrBuff.toString().toLowerCase());
		return md5StrBuff.toString().toLowerCase();
	}

	/**
	 * 去掉字符串中的 空格、回车、换行符、制表符等空白符
	 * 
	 * @param str
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");// 空格、回车、换行符、制表符
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	public static int getLawyerid(Context context) {
		if (Globals.lawyerid > 0) {
			return Globals.lawyerid;
		} else {
			SharedPreferences spf_user = Utils.getSharedPreferences(context,
					Globals.SPID_LVGOU_USERS);
			String memberid = spf_user.getString(Globals.SP_USERID,
					Globals.lawyerid + "");
			return Integer.parseInt(memberid);
		}
	}

	/**
	 * 友盟测试设备信息获取方法
	 * 
	 * @param context
	 */
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setListViewHeightBasedOnChildren(ListView attlist) {
		ListAdapter listAdapter = attlist.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, attlist);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = attlist.getLayoutParams();
		params.height = totalHeight
				+ (attlist.getDividerHeight() * (listAdapter.getCount() - 1));
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		attlist.setLayoutParams(params);
	}

}
