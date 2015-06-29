package com.lvgou.lawyer.studio;

import android.os.Environment;

/**
 * 全局变量
 * 
 * @author xiangyu
 * 
 */
public final class Globals {
	public static final String ENV = "1"; // 运行环境:1-测试 2-正式
	public static final String ENTRANCEID = ""; // 渠道id
	public static final String SECRETKEY = "s0sdf4hyeww68sgn65f45mw9ss"; // 秘钥
	public static String lvgouAPI = "http://lg.lvgou.com/mobile/"; // 测试环境
//	public static String lvgouAPI = "http://192.168.0.7/mobile/"; // 测试环境

	// 设备相关信息
	public static float density = 0; // 设备屏幕密度系数，默认为1
	public static int widthPixels; // 设备屏幕宽度（像素）
	public static int heightPixels; // 设备屏幕高度（像素）
	public static String MAC;
	// 接口固定参数
	public static final int PLATFORM = 1; // 运行平台:1-Android phone 2-iphone
	public static final int SITE = 1; // 应用站点:1-律师端 2-用户端
	public static String VERSION; // 当前版本,在welcome中赋值
	public static String fixed; // 固定参数拼接串，每个接口都要加，welcome中赋值

	// 本地数据文件key
	public static final String SPID_LVGOU_PARAMS = "lvgouparams";
	public static final String SPID_LVGOU_USERS = "lvgouusers";
	public static final String SPID_LVGOU_PUSH = "lvgoupush";
	public static final String SP_USERID = "userid"; // 用户ID
	public static final String SP_USERAUTH = "auth"; // 用户状态
	public static final String SP_USERNAME = "username"; // 用户名
	public static final String SP_PASSWORD = "password"; // 密码
	public static final String SP_BAIDUAPPID = "appid"; // 应用ID_百度云推送
	public static final String SP_FIXED = "fixed";
	// public static final String SP_BAIDUUSERID = "baidu_userid"; // 用户ID_百度云推送
	// public static final String SP_BAIDUCHANNELID = "channelId"; // 通道ID_百度云推送
	public static final String SP_BINDBAIDU_FLAG = "bind_flag"; // 绑定云推送标识
	public static final String SP_BINDSERVER_FLAG = "bind_server_flag"; // 推送后绑定服务器标标识

	// 用户信息
	public static int lawyerid;
	public static int auth = 0; // 用户状态 0:未通过审核 1:通过审核
	public static int from = 1;

	// 存储卡根目录及应用目录
	public static final String SDPATH = Environment
			.getExternalStorageDirectory().getPath();
	public static final String FILEPATH = SDPATH + "/lvgou/";
}
