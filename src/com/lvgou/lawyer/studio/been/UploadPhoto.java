package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;

public class UploadPhoto {
	private String url;
	
	public String getUrl() {
		url = Globals.lvgouAPI + "passport/modifyVerify";
		LogUtils.i(url);
		return url;
	}

//	private String getKey() {
//		params = new String[] { mobile + "", type + "" };
//		return Utils.encrypt(params);
//	}
}
