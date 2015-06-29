package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class SendCode {
	private int code;
	private String[] params;
	
	private String mobile;
	private String type; //lawyerreg:注册验证码

	private String url;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String getUrl() {
		url = Globals.lvgouAPI + "commonly/sendCode?mobile=" + mobile
				+ "&type=" + type + "&signature=" + getKey()
				+ Globals.fixed;
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { mobile + "", type + "" };
		return Utils.encrypt(params);
	}
}
