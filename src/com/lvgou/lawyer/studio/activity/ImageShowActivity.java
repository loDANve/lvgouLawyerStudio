package com.lvgou.lawyer.studio.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.umeng.analytics.MobclickAgent;

public class ImageShowActivity extends Activity implements OnClickListener {

	private Context mContext;

	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.img)
	private ImageView img;
	private String path;
	private Bitmap bitmap;
	private DisplayMetrics dm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.imageshow_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			path = bundle.getString("path");
		}
		dm = new DisplayMetrics();
		initView();
	}

	private void initView() {
		titletext.setText("图片预览");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		BitmapUtils bitmaputils = new BitmapUtils(mContext, Globals.FILEPATH);
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setAutoRotation(true);
		// BitmapSize size = new BitmapSize(720, 0);
		// config.setBitmapMaxSize(size);
		bitmaputils.display(img, path, config);

	}

	private class ImageCallBack extends BitmapLoadCallBack<View> {

		@Override
		public void onLoadCompleted(View arg0, String arg1, Bitmap arg2,
				BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
			LogUtils.e(arg1 + "");
			arg0.setBackground(new BitmapDrawable(getResources(), arg2));
			Toast.makeText(mContext, "图片载入成功", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
			LogUtils.e(arg1 + "");
			Toast.makeText(mContext, "图片载入失败，请重新选择", Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
