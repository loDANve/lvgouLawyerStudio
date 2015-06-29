package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class Register {
	private int code;
	private String[] params;

	private String mobile;
	private String verification;
	private String password;

	private String url;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		url = Globals.lvgouAPI + "passport/addLawyer?mobile=" + mobile + "&verification="
				+ verification + "&password=" + password + "&signature=" + getKey()
				+ Globals.fixed;
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { mobile, verification, password };
		return Utils.encrypt(params);
	}
}
