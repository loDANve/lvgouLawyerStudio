package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class MyCenter {
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
	private int unreadNums; // 未读公告数
	private String balance; // 余额（开通或锁定）
	private int openaccount; // 资金账户 0未开通：1开通;2锁定
	private int bankNums; // 银行卡数
	private String serviceTel; // 客服热线

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
	/**
	 * 未读公告数
	 * @return
	 */
	public int getUnreadNums() {
		return unreadNums;
	}

	public void setUnreadNums(int unreadNums) {
		this.unreadNums = unreadNums;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public int getOpenaccount() {
		return openaccount;
	}

	public void setOpenaccount(int openaccount) {
		this.openaccount = openaccount;
	}

	public int getBankNums() {
		return bankNums;
	}

	public void setBankNums(int bankNums) {
		this.bankNums = bankNums;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
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
