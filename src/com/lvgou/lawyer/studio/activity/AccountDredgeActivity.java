package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.umeng.analytics.MobclickAgent;
/**
 * 开通资金账户
 * @author xiangyu
 *
 * @notes Created on 2014-9-16
 */
public class AccountDredgeActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
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
	@ViewInject(R.id.identityid)
	private EditText identityid;
	@ViewInject(R.id.identityid_img)
	private ImageView identityid_img;
	@ViewInject(R.id.pwd)
	private EditText pwd_ext;
	@ViewInject(R.id.pwd_img)
	private ImageView pwd_img;
	@ViewInject(R.id.dredge)
	private Button dredge;
	@ViewInject(R.id.pwd_show_img)
	private ImageButton pwdshow_img;

	private String mUsername;
	private boolean isshow = false;

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
		setContentView(R.layout.account_dredge_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("开通资金账户");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		dredge.setOnClickListener(this);
		identityid.setOnFocusChangeListener(this);
		username_ext.setOnFocusChangeListener(this);
		pwdshow_img.setOnClickListener(this);
		pwd_ext.setOnFocusChangeListener(this);
		pwd_ext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mUsername = username_ext.getText().toString().trim();
				if (null != s && s.length() > 1 && null != mUsername
						&& mUsername.length() > 0) {
					dredge.setBackgroundResource(R.drawable.register_login_btn);
					dredge.setTextColor(getResources().getColor(R.color.white));
					dredge.setClickable(true);
				} else {
					dredge.setBackgroundResource(R.drawable.register_btn_unable);
					dredge.setTextColor(getResources().getColor(
							R.color.btn_text));
					dredge.setClickable(false);
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

		Intent intent = new Intent(AccountDredgeActivity.this,
				MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(AccountDredgeActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.dredge:
			intent = new Intent(AccountDredgeActivity.this,
					AccountActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.pwd_show_img:
			if (isshow) {
				// 设置为秘闻显示
				pwd_ext.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				pwdshow_img.setBackgroundResource(R.drawable.pwd_hide);
			} else {
				// 设置为明文显示
				pwd_ext.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
				pwdshow_img.setBackgroundResource(R.drawable.pwd_show);
			}
			isshow = !isshow;
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
						.setBackgroundResource(R.drawable.realname_icon_click);
			} else {
				username_img
						.setBackgroundResource(R.drawable.realname_icon_def);
			}
			break;
		case R.id.identityid:
			if (hasFocus) {
				identityid_img
						.setBackgroundResource(R.drawable.bankcard_icon_click);
			} else {
				identityid_img
						.setBackgroundResource(R.drawable.bankcard_icon_def);
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
