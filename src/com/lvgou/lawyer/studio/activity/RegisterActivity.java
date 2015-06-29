package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
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
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.Register;
import com.lvgou.lawyer.studio.been.SendCode;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class RegisterActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
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
	@ViewInject(R.id.verification)
	private EditText verification_ext;
	@ViewInject(R.id.register)
	private Button register_btn;
	@ViewInject(R.id.agree)
	private ImageButton agreement_img;
	@ViewInject(R.id.sendverification)
	private Button sendverification;

	private String mUsername;
	private String mPwd;
	private String mVerification;
	private SendCode sc;
	private Register reg;
	private HttpUtils http;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(RegisterActivity.this,
						IdentificationActivity.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_SHORT).show();
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
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.register_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("注册");
		actiontext.setText("登录");
		action_rl.setVisibility(View.VISIBLE);
		action_rl.setOnClickListener(this);
		username_ext.setOnFocusChangeListener(this);
		pwd_ext.setOnFocusChangeListener(this);
		register_btn.setOnClickListener(this);
		sendverification.setOnClickListener(this);
		verification_ext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mUsername = username_ext.getText().toString().trim();
				if (null != s && s.length() == 4 && null != mUsername
						&& mUsername.length() > 0) {
					register_btn
							.setBackgroundResource(R.drawable.register_login_btn);
					register_btn.setTextColor(getResources().getColor(
							R.color.white));
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

	private void doRegister() {
		mUsername = username_ext.getText().toString().trim();
		mPwd = pwd_ext.getText().toString().trim();
		mVerification = verification_ext.getText().toString().trim();

		if (mUsername == null || mUsername.length() < 1) {
			return;
		}

		if (mPwd == null || mUsername.length() < 1) {
			return;
		}

		if (mVerification == null || mUsername.length() < 1) {
			return;
		}
		reg = new Register();
		reg.setMobile(mUsername);
		reg.setVerification(mVerification);
		reg.setPassword(mPwd);
		http = new HttpUtils(5000);
		http.send(HttpRequest.HttpMethod.GET, reg.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == RegisterActivity.this
								|| RegisterActivity.this.isFinishing()) {
							return;
						}
						reg = Utils.json2Object(responseInfo.result.getBytes(),
								reg);
						handler.sendEmptyMessage(reg.getCode());
					}
				});
	}

	private void sendCode() {
		mUsername = username_ext.getText().toString().trim();
		sc = new SendCode();
		sc.setMobile(mUsername);
		sc.setType("lawyerreg");
		http = new HttpUtils(5000);
		http.send(HttpRequest.HttpMethod.GET, sc.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == RegisterActivity.this
								|| RegisterActivity.this.isFinishing()) {
							return;
						}
						sc = Utils.json2Object(responseInfo.result.getBytes(),
								sc);
						if (null != sc) {
							if (sc.getCode() == 1) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(sc.getCode());
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(RegisterActivity.this, LoginPwdActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.register:
			doRegister();
			break;
		case R.id.sendverification:
			sendCode();
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
