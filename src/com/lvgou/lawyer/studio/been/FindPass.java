package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class FindPass {
	/** 
	 * 
	 * 返回参数
	 */
	private int code;
	private String msg;
	/**
	 * 传入参数
	 */
	private int step;
	private String mobile;
	private String verification;
	private String password;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

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

	private String[] params;
	/**
	 * url
	 */
	private String url;

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String getUrl() {
		url = Globals.lvgouAPI + "passport/findPass?step=" + step + "&mobile="
				+ mobile + "&verification=" + verification + "&signature="
				+ getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { step + "", mobile, verification };
		return Utils.encrypt(params);
	}
}
