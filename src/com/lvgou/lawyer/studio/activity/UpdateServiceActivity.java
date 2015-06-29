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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.R.id;
import com.lvgou.lawyer.studio.adapter.UpadateServiceAttListAdapter;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class UpdateServiceActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.actionbtn)
	private ImageButton actionbtn;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.attlist)
	private ListView attlist;
	@ViewInject(R.id.selectfile)
	private ImageButton selectfile;

	private UpadateServiceAttListAdapter attAdapter;

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
		setContentView(R.layout.updateservice_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		titletext.setText("更新服务");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		selectfile.setOnClickListener(this);
		attAdapter = new UpadateServiceAttListAdapter(mContext, null);
		attlist.setAdapter(attAdapter);
		Utils.setListViewHeightBasedOnChildren(attlist);
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
		case R.id.selectfile:
			intent = new Intent(UpdateServiceActivity.this,
					FileSelectActivity.class);
			startActivity(intent);
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
