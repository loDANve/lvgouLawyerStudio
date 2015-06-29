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
 * 提现
 * @author xiangyu
 *
 * @notes Created on 2014-9-16
 */
public class AccountWithdrawActivity extends Activity implements
		OnClickListener {

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
	@ViewInject(R.id.count)
	private EditText count;
	@ViewInject(R.id.count_img)
	private ImageView count_img;
	@ViewInject(R.id.withdraw)
	private Button withdraw;

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
		setContentView(R.layout.account_withdraw_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("提现");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		withdraw.setOnClickListener(this);
		count.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (null != s && s.length() > 0) {
					withdraw.setBackgroundResource(R.drawable.register_login_btn);
					withdraw.setTextColor(getResources()
							.getColor(R.color.white));
					withdraw.setClickable(true);
				} else {
					withdraw.setBackgroundResource(R.drawable.register_btn_unable);
					withdraw.setTextColor(getResources().getColor(
							R.color.btn_text));
					withdraw.setClickable(false);
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

	private void doNext() {
		if (mUsername == null || mUsername.length() <= 0) {
			handler.sendEmptyMessage(1002);
			return;
		}

		Intent intent = new Intent(AccountWithdrawActivity.this,
				MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(AccountWithdrawActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.dredge:
			intent = new Intent(AccountWithdrawActivity.this,
					AccountActivity.class);
			startActivity(intent);
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
