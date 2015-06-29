package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class Home {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	private String unconfirmordernum; //新订单数量
	private String schedule; //档期
	private int lastMonthIncome; //上月收入
	private String totalIncome; //全部收入
	/**
	 * 传入参数
	 */
	private int lawyerid;

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

	public String getUnconfirmordernum() {
		return unconfirmordernum;
	}

	public void setUnconfirmordernum(String unconfirmordernum) {
		this.unconfirmordernum = unconfirmordernum;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public int getLastMonthIncome() {
		return lastMonthIncome;
	}

	public void setLastMonthIncome(int lastMonthIncome) {
		this.lastMonthIncome = lastMonthIncome;
	}

	public int getLawyerid() {
		return lawyerid;
	}

	public void setLawyerid(int lawyerid) {
		this.lawyerid = lawyerid;
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
		url = Globals.lvgouAPI + "index/index?lawyerid=" + lawyerid
				+ "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { lawyerid + "" };
		return Utils.encrypt(params);
	}
}
