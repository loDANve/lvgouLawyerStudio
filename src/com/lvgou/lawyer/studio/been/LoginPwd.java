package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class LoginPwd {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	private int lawyerid; // 律师ID。失败时该参数不传
	private int authentication; // 0未提交认证资料;1已提交待审核;2通过认证;3未通过;4解除合作
	private int authed;// 0未通过过;1已通过过

	/**
	 * 传入参数
	 */
	private String mobile;
	private String password;
	private String[] params;
	/**
	 * url
	 */
	private String url;

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

	public int getLawyerid() {
		return lawyerid;
	}

	public void setLawyerid(int lawyerid) {
		this.lawyerid = lawyerid;
	}

	public int getAuthentication() {
		return authentication;
	}

	public void setAuthentication(int authentication) {
		this.authentication = authentication;
	}

	public int getAuthed() {
		return authed;
	}

	public void setAuthed(int authed) {
		this.authed = authed;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String getUrl() {
		url = Globals.lvgouAPI + "passport/login?mobile=" + mobile + "&password="
				+ password + "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { mobile + "", password + "" };
		return Utils.encrypt(params);
	}
}
