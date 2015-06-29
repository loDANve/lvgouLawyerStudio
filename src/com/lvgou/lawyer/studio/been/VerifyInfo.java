package com.lvgou.lawyer.studio.been;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.utils.Utils;

public class VerifyInfo {
	/**
	 * 返回参数
	 */
	private int code;
	private String msg;
	private String userImg; // 头像(todo)
	private String realname; // 姓名
	private String mobile; // 手机号
	private String areatitle;	//所在地区名称（例：北京-北京市）
	private int province;	//省id
	private int city;	//城市id
	private String lawyeroffice;	//所在律所
	private String lawyerofficeaddr;	//办公地址
	private String lawyerno;	//证号
	private String productcategorytitle;	//擅长领域名称（例：法律咨询、记账、公司注册）
	private String productcategory;	//领域id
	private String serviceareatitle;	//服务地区名称（例如：北京、天津、辽宁省、江苏省）
	private String servicearea;	//地区id
	private String lawyerImg;	//本人照片（todo）
	private String jobpic;	//执业证书(todo)
	private String email;	//邮箱


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

	public String getAreatitle() {
		return areatitle;
	}

	public void setAreatitle(String areatitle) {
		this.areatitle = areatitle;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLawyeroffice() {
		return lawyeroffice;
	}

	public void setLawyeroffice(String lawyeroffice) {
		this.lawyeroffice = lawyeroffice;
	}

	public String getLawyerofficeaddr() {
		return lawyerofficeaddr;
	}

	public void setLawyerofficeaddr(String lawyerofficeaddr) {
		this.lawyerofficeaddr = lawyerofficeaddr;
	}

	public String getLawyerno() {
		return lawyerno;
	}

	public void setLawyerno(String lawyerno) {
		this.lawyerno = lawyerno;
	}

	public String getProductcategorytitle() {
		return productcategorytitle;
	}

	public void setProductcategorytitle(String productcategorytitle) {
		this.productcategorytitle = productcategorytitle;
	}

	public String getProductcategory() {
		return productcategory;
	}

	public void setProductcategory(String productcategory) {
		this.productcategory = productcategory;
	}

	public String getServiceareatitle() {
		return serviceareatitle;
	}

	public void setServiceareatitle(String serviceareatitle) {
		this.serviceareatitle = serviceareatitle;
	}

	public String getServicearea() {
		return servicearea;
	}

	public void setServicearea(String servicearea) {
		this.servicearea = servicearea;
	}

	public String getLawyerImg() {
		return lawyerImg;
	}

	public void setLawyerImg(String lawyerImg) {
		this.lawyerImg = lawyerImg;
	}

	public String getJobpic() {
		return jobpic;
	}

	public void setJobpic(String jobpic) {
		this.jobpic = jobpic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		url = Globals.lvgouAPI + "user/verify?lawyerid=" + lawyerid
				+ "&signature=" + getKey();
		LogUtils.i(url);
		return url;
	}

	private String getKey() {
		params = new String[] { lawyerid + "" };
		return Utils.encrypt(params);
	}
}
