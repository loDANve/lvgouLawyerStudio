package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class SetSchedule {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	/**
	 * 传入参数
	 */
	private int lawyerid;
	private int setSchedule;

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

	public int getSetSchedule() {
		return setSchedule;
	}

	public void setSetSchedule(int setSchedule) {
		this.setSchedule = setSchedule;
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
		url = Globals.lvgouAPI + "index/setSchedule?lawyerid=" + lawyerid
				+ "&setSchedule=" + setSchedule + "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { lawyerid + "", setSchedule + "" };
		return Utils.encrypt(params);
	}
}
