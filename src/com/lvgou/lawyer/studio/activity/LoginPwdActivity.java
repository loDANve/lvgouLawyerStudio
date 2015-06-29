package com.lvgou.lawyer.studio.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.LoginPwd;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class LoginPwdActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.login_phone)
	private TextView login_phone;
	@ViewInject(R.id.forget_pwd)
	private TextView forget_pwd;
	@ViewInject(R.id.hint_text)
	private TextView hint_text;
	@ViewInject(R.id.hint_img)
	private ImageView hint_img;
	@ViewInject(R.id.hint_ll)
	private LinearLayout hint_ll;
	@ViewInject(R.id.username)
	private EditText username_ext;
	@ViewInject(R.id.username_img)
	private ImageView username_img;
	@ViewInject(R.id.pwd)
	private EditText pwd_ext;
	@ViewInject(R.id.pwd_img)
	private ImageView pwd_img;
	@ViewInject(R.id.login)
	private Button login_btn;

	private String mUsername;
	private String mPassword;
	private Map<String, String> spMap;

	private LoginPwd lp;
	private HttpUtils http;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			LoginPwd l = (LoginPwd) msg.obj;
			if (l == null) {
				return;
			}
			switch (l.getCode()) {
			case 1:

				break;
			case 1002:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("手机号不能为空");
				hint_ll.setVisibility(View.VISIBLE);
				break;
			case 1003:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("密码不能为空");
				hint_ll.setVisibility(View.VISIBLE);
				break;
			default:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText(lp.getMsg());
				hint_ll.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login_pwd_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("登录");
		actiontext.setText("注册");
		action_rl.setVisibility(View.VISIBLE);
		action_rl.setOnClickListener(this);
		login_phone.setOnClickListener(this);
		forget_pwd.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		username_ext.setOnFocusChangeListener(this);
		pwd_ext.setOnFocusChangeListener(this);
		pwd_ext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mUsername = username_ext.getText().toString().trim();
				if (null != s && s.length() > 0 && null != mUsername
						&& mUsername.length() > 0) {
					login_btn
							.setBackgroundResource(R.drawable.register_login_btn);
					login_btn.setTextColor(getResources().getColor(
							R.color.white));
					login_btn.setClickable(true);
				} else {
					login_btn
							.setBackgroundResource(R.drawable.register_btn_unable);
					login_btn.setTextColor(getResources().getColor(
							R.color.btn_text));
					login_btn.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void doLogin() {
		mUsername = username_ext.getText().toString().trim();
		mPassword = pwd_ext.getText().toString().trim();
		if (mUsername == null || "".equals(mUsername)) {
			handler.sendEmptyMessage(1002);
			return;
		}
		if (mPassword == null || "".equals(mPassword)) {
			handler.sendEmptyMessage(1003);
			return;
		}

		lp = new LoginPwd();
		lp.setMobile(mUsername);
		lp.setPassword(mPassword);

		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, lp.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == LoginPwdActivity.this
								|| LoginPwdActivity.this.isFinishing()) {
							return;
						}
						lp = Utils.json2Object(responseInfo.result.getBytes(),
								lp);
						if (lp != null) {
							if (lp.getCode() == 1) {
								Globals.lawyerid = lp.getLawyerid();
								spMap = new HashMap<String, String>();
								spMap.put(Globals.SP_USERID, lp.getLawyerid()
										+ "");
								Utils.editSharedPreferences(mContext, spMap,
										Globals.SPID_LVGOU_USERS);
								Intent intent = new Intent(mContext,
										MainActivity.class);
								startActivity(intent);
								finish();
								Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(mContext, lp.getMsg(), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(LoginPwdActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.login_phone:
			intent = new Intent(LoginPwdActivity.this, LoginPhoneActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.forget_pwd:
			intent = new Intent(LoginPwdActivity.this, FindPwdActivity.class);
			startActivity(intent);
			break;
		case R.id.login:
			doLogin();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.username:
			if (hasFocus) {
				username_img
						.setBackgroundResource(R.drawable.username_img_click);
			} else {
				username_img.setBackgroundResource(R.drawable.username_img_def);
			}
			break;
		case R.id.pwd:
			if (hasFocus) {
				pwd_img.setBackgroundResource(R.drawable.pwd_img_click);
			} else {
				pwd_img.setBackgroundResource(R.drawable.pwd_img_def);
			}
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
