package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.umeng.analytics.MobclickAgent;

public class LoginPhoneActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.login_pwd)
	private TextView login_pwd;
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
	@ViewInject(R.id.login)
	private Button login_btn;
	@ViewInject(R.id.verification)
	private EditText verification;

	private String mUsername;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				break;
			case 1002:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("手机号不能为空");
				hint_ll.setVisibility(View.VISIBLE);
				break;
			case 1003:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("验证码不能为空");
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
		setContentView(R.layout.login_phone_activity);
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
		login_btn.setClickable(false);
		login_btn.setOnClickListener(this);
		action_rl.setOnClickListener(this);
		login_pwd.setOnClickListener(this);
		username_ext.setOnFocusChangeListener(this);
		verification.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mUsername = username_ext.getText().toString().trim();
				if (null != s && s.length() == 4 && null != mUsername
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
		if (mUsername == null || mUsername.length() <= 0) {
			handler.sendEmptyMessage(1002);
			return;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(LoginPhoneActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.login_pwd:
			intent = new Intent(LoginPhoneActivity.this, LoginPwdActivity.class);
			startActivity(intent);
			finish();
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
