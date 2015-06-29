package com.lvgou.lawyer.studio.been;

import java.util.List;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class NoticeList {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	private List<NoticeList_Item> list;
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

	public int getLawyerid() {
		return lawyerid;
	}

	public void setLawyerid(int lawyerid) {
		this.lawyerid = lawyerid;
	}

	public List<NoticeList_Item> getList() {
		return list;
	}

	public void setList(List<NoticeList_Item> list) {
		this.list = list;
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
		url = Globals.lvgouAPI + "user/notice?lawyerid=" + lawyerid
				+ "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { lawyerid + "" };
		return Utils.encrypt(params);
	}
}
