package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class PersonalInfo {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	private String userImg; // 头像(todo)
	private String realname; // 姓名
	private String mobile; // 手机号
	private int lawyerlevel; // 等级
	private String levelNotice; // 等级说明
	private int authentication; //认证状态（ 0未提交认证资料；1已提交待审核；2通过认证；3未通过；4解除合作）
	private int openaccount;	// 资金账户 0未开通：1开通;2锁定


	/**
	 * 传入参数
	 */
	private int lawyerid;
	private String[] params;

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

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getLawyerlevel() {
		return lawyerlevel;
	}

	public void setLawyerlevel(int lawyerlevel) {
		this.lawyerlevel = lawyerlevel;
	}

	public String getLevelNotice() {
		return levelNotice;
	}

	public void setLevelNotice(String levelNotice) {
		this.levelNotice = levelNotice;
	}

	public int getAuthentication() {
		return authentication;
	}

	public void setAuthentication(int authentication) {
		this.authentication = authentication;
	}

	public int getOpenaccount() {
		return openaccount;
	}

	public void setOpenaccount(int openaccount) {
		this.openaccount = openaccount;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * url
	 */
	private String url;

	public String getUrl() {
		url = Globals.lvgouAPI + "user/info?lawyerid=" + lawyerid
				+ "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { lawyerid + "" };
		return Utils.encrypt(params);
	}
}
