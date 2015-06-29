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
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class EditPwdActivity extends Activity implements OnClickListener,
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
	@ViewInject(R.id.pwd)
	private EditText pwd_ext;
	@ViewInject(R.id.pwd_img)
	private ImageView pwd_img;
	@ViewInject(R.id.finish)
	private Button finish_btn;
	@ViewInject(R.id.pwd_show_img)
	private ImageButton pwdshow_img;

	private String mUsername;
	private boolean isshow = false;
	private FindPass fp = null;
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
		setContentView(R.layout.editpwd_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("修改密码");
		finish_btn.setClickable(false);
		finish_btn.setOnClickListener(this);
		pwd_ext.setOnFocusChangeListener(this);
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		pwdshow_img.setOnClickListener(this);
		pwd_ext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (null != s && s.length() >= 6) {
					finish_btn
							.setBackgroundResource(R.drawable.register_login_btn);
					finish_btn.setTextColor(getResources().getColor(
							R.color.white));
					finish_btn.setClickable(true);
				} else {
					finish_btn
							.setBackgroundResource(R.drawable.register_btn_unable);
					finish_btn.setTextColor(getResources().getColor(
							R.color.btn_text));
					finish_btn.setClickable(false);
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

	private void doNext(){
//		mUsername = username_ext.getText().toString().trim();
		
		fp = new FindPass();
//		fp.setMobile(mUsername);
//		fp.setPassword(password);
		fp.setStep(2);
		http = new HttpUtils(5000);
		http.send(HttpRequest.HttpMethod.GET, fp.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == EditPwdActivity.this
								|| EditPwdActivity.this.isFinishing()) {
							return;
						}
						fp = Utils.json2Object(responseInfo.result.getBytes(),
								fp);
						if (null != fp) {
							if (fp.getCode() == 1) {
								Intent intent = null;
								intent = new Intent(EditPwdActivity.this, EditPwdActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.finish:
//			intent = new Intent(EditPwdActivity.this, LoginPwdActivity.class);
//			startActivity(intent);
			finish();
			break;
		case R.id.back_img:
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
				pwd_ext.setBackgroundResource(R.drawable.pwd_img_click);
			} else {
				pwd_ext.setBackgroundResource(R.drawable.pwd_img_def);
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
