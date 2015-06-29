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
import com.lvgou.lawyer.studio.been.VerifyInfo;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class IdentificationStateActivity extends Activity implements
		OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.realname)
	private TextView realname;	//真实姓名
	@ViewInject(R.id.location)	
	private TextView location;	//所在地区
	@ViewInject(R.id.office)
	private TextView office;	//所在律所
	@ViewInject(R.id.office_address)
	private TextView office_address;	//办公地址
	@ViewInject(R.id.credential_address)
	private TextView credential_address;	//职业正好
	@ViewInject(R.id.field)	
	private TextView field;	//擅长领域
	@ViewInject(R.id.service)
	private TextView service;	//服务地区
	@ViewInject(R.id.credential_img)
	private ImageView credential_img;	//执业证书
	@ViewInject(R.id.photo_img)
	private ImageView photo_img;	//照片
	
	private VerifyInfo vi;
	private HttpUtils http;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				break;
			case 1002:
				break;
			case 1003:
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
		setContentView(R.layout.identification_state_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("审核状态");
		actiontext.setText("修改信息");
		action_rl.setVisibility(View.VISIBLE);
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		getMyCenterData();
	}

	private void getMyCenterData() {
		vi = new VerifyInfo();
		vi.setLawyerid(Utils.getLawyerid(mContext));
		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, vi.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == IdentificationStateActivity.this
								|| IdentificationStateActivity.this
										.isFinishing()) {
							return;
						}
						vi = Utils.json2Object(responseInfo.result.getBytes(),
								vi);
						if (null != vi && vi.getCode() == 1) {
							realname.setText(vi.getRealname());
							location.setText(vi.getAreatitle());
							office.setText(vi.getLawyeroffice());
							office_address.setText(vi.getLawyerofficeaddr());
							credential_address.setText(vi.getLawyerno());
							field.setText(vi.getProductcategorytitle());
							service.setText(vi.getServiceareatitle());
							BitmapUtils bu = new BitmapUtils(mContext);
							bu.display(credential_img, vi.getLawyerImg());
							bu.display(photo_img, vi.getJobpic());
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.finish:
			// intent = new Intent(EditPwdActivity.this,
			// LoginPwdActivity.class);
			// startActivity(intent);
			finish();
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
