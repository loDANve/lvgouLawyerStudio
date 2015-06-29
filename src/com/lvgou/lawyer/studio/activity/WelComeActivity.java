package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class WelComeActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.upload_btn)
	private Button upload_btn;
	
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		mContext = this;
		ViewUtils.inject(this);
		System.out.println(Utils.getDeviceInfo(mContext));// 获取测试设备信息，用于友盟添加测试设备
		MobclickAgent.updateOnlineConfig(mContext);
		initUmengUpdate();
		UmengUpdateAgent.update(this);// 调用友盟自动更新
		initView();
	}

	private void initView() {
		upload_btn.setOnClickListener(this);
	}

	/**
	 * 初始化友盟自动更新配置
	 */
	private void initUmengUpdate() {
		UmengUpdateAgent.setUpdateCheckConfig(false);// 禁用toast调试提示
		UmengUpdateAgent.setDeltaUpdate(false); // 关闭增量更新
		UmengUpdateAgent.setUpdateAutoPopup(false); // 禁止弹出更新窗口
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			// 自定义更新监听
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // 有更新
					Toast.makeText(mContext, "发现新版本", Toast.LENGTH_SHORT)
							.show();
					// UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
					verificationLogin();
					break;
				case UpdateStatus.No: // 无更新
					verificationLogin();
					Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // 无wifi
					verificationLogin();
					Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新",
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // 访问超时
					verificationLogin();
					Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	private void verificationLogin() {
		// 查看登录状态
		sp = Utils.getSharedPreferences(this,
				Globals.SPID_LVGOU_USERS);
		String userid = sp.getString(Globals.SP_USERID, "");
		if ("".equals(userid) || "0".equals(userid)) { // 未登录,跳转到登录页
			goToLogin();
//			goToMain();
		} else { // 已登录,跳转到主页
			Globals.from = 1;
			Globals.lawyerid = Integer.parseInt(userid);
			Globals.auth = Integer.parseInt(sp.getString(Globals.SP_USERAUTH,
					"0"));
			goToMain();
		}
	}

	private void goToLogin() {
		Intent intent = new Intent(WelComeActivity.this, LoginPwdActivity.class);
		startActivity(intent);
		finish();
	}

	private void goToMain() {
		Intent intent = new Intent(WelComeActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upload_btn:
			Intent intent = new Intent(WelComeActivity.this,
					UploadActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@ViewInject(R.id.checkupdate_btn)
	private Button checkupdate_btn;

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
