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
import com.lvgou.lawyer.studio.been.FindPass;
import com.lvgou.lawyer.studio.been.Register;
import com.lvgou.lawyer.studio.been.SendCode;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class FindPwdActivity extends Activity implements OnClickListener,
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
	@ViewInject(R.id.next)
	private Button next_btn;
	@ViewInject(R.id.sendverification)
	private Button sendverification;
	@ViewInject(R.id.verification)
	private EditText verification;

	private String mUsername;
	private SendCode sc;
	private FindPass fp;
	private HttpUtils http;

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
		setContentView(R.layout.findpwd_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("找回密码");
		next_btn.setClickable(false);
		next_btn.setOnClickListener(this);
		username_ext.setOnFocusChangeListener(this);
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		sendverification.setOnClickListener(this);
//		verification.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				mUsername = username_ext.getText().toString().trim();
//				if (null != s && s.length() == 4 && null != mUsername
//						&& mUsername.length() > 0) {
//					next_btn
//							.setBackgroundResource(R.drawable.register_login_btn);
//					next_btn.setTextColor(getResources().getColor(
//							R.color.white));
//					next_btn.setClickable(true);
//				} else {
//					next_btn
//							.setBackgroundResource(R.drawable.register_btn_unable);
//					next_btn.setTextColor(getResources().getColor(
//							R.color.btn_text));
//					next_btn.setClickable(false);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
	}
	
	private void sendCode() {
		mUsername = username_ext.getText().toString().trim();
		sc = new SendCode();
		sc.setMobile(mUsername);
		sc.setType("validatecodelawyer");
		http = new HttpUtils(5000);
		http.send(HttpRequest.HttpMethod.GET, sc.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == FindPwdActivity.this
								|| FindPwdActivity.this.isFinishing()) {
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
	
	private void doNext(){
		mUsername = username_ext.getText().toString().trim();
		
		fp = new FindPass();
		fp.setMobile(mUsername);
		fp.setVerification(verification.getText().toString().trim());
		fp.setStep(1);
		http = new HttpUtils(5000);
		http.send(HttpRequest.HttpMethod.GET, fp.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == FindPwdActivity.this
								|| FindPwdActivity.this.isFinishing()) {
							return;
						}
						fp = Utils.json2Object(responseInfo.result.getBytes(),
								fp);
						if (null != fp) {
							if (fp.getCode() == 1) {
								Intent intent = null;
								intent = new Intent(FindPwdActivity.this, EditPwdActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.next:
			doNext();
			break;
		case R.id.back_img:
			finish();
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
