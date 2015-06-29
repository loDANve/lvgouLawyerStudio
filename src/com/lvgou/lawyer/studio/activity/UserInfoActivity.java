package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.MyCenter;
import com.lvgou.lawyer.studio.been.PersonalInfo;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人信息
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-9-19
 */
public class UserInfoActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.userlevel_rl)
	private RelativeLayout userlevel_rl;
	@ViewInject(R.id.identificationstate_rl)
	private RelativeLayout identificationstate_rl;
	@ViewInject(R.id.balance_rl)
	private RelativeLayout balance_rl;
	@ViewInject(R.id.realname)
	private TextView realname;
	@ViewInject(R.id.phonenumber)
	private TextView phonenumber;
	@ViewInject(R.id.userlevel)
	private TextView userlevel;
	@ViewInject(R.id.headimg)
	private ImageView headimg;
	@ViewInject(R.id.identificationstate)
	private TextView identificationstate;
	@ViewInject(R.id.balance)
	private TextView balance;

	private PersonalInfo pi;
	private HttpUtils http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.userinfo_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("个人信息");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		userlevel_rl.setOnClickListener(this);
		getMyCenterData();
	}

	private void getMyCenterData() {
		pi = new PersonalInfo();
		pi.setLawyerid(Utils.getLawyerid(mContext));
		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, pi.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == UserInfoActivity.this
								|| UserInfoActivity.this.isFinishing()) {
							return;
						}
						pi = Utils.json2Object(responseInfo.result.getBytes(),
								pi);
						if (null != pi && pi.getCode() == 1) {
							BitmapUtils bu = new BitmapUtils(mContext);
							bu.display(headimg, pi.getUserImg());
							bu = null;
							realname.setText(pi.getRealname());
							phonenumber.setText(pi.getMobile());
							userlevel.setText(pi.getLawyerlevel() + "("
									+ pi.getLevelNotice() + ")");
							switch (pi.getAuthentication()) {
							case 1:
								identificationstate.setText("已提交待审核");
								break;
							case 2:
								identificationstate.setText("通过认证");
								break;
							case 3:
								identificationstate.setText("未通过");
								break;
							case 4:
								identificationstate.setText("解除合作");
								break;
							default:
								identificationstate.setText("其他");
								break;
							}
							if(pi.getOpenaccount() == 0){
								balance.setText("已开通");
							}else{
								balance.setText("未开通");
							}

						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.userlevel_rl:
			intent = new Intent(UserInfoActivity.this,
					LevelChangeActivity.class);
			startActivity(intent);
			// finish();
			break;
		case R.id.back_img:
			finish();
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
