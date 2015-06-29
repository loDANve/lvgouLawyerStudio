package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.logout_rl)
	private RelativeLayout logout_rl;

	AlertDialog retryAuto_alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.setting_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("系统设置");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		logout_rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(SettingActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.logout_rl:
			Builder retryAuto_builder = new AlertDialog.Builder(mContext)
					.setTitle("提 示")
					.setMessage("确认要退出当前用户么！")
					.setNegativeButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Utils.getSharedPreferences(mContext,
											Globals.SPID_LVGOU_USERS).edit()
											.clear();
									Intent intent = new Intent(mContext,LoginPwdActivity.class);
									startActivity(intent);
									dialog.cancel();
								}
							})
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							});
			retryAuto_alert = retryAuto_builder.create();
			retryAuto_alert.show();
			break;
		default:
			break;
		}
	}

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

}
