package com.lvgou.lawyer.studio.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.fragment.InServiceProductDetailFragment;
import com.lvgou.lawyer.studio.fragment.ServiceLogFragment;
import com.umeng.analytics.MobclickAgent;

public class InserviceDetailActivity extends FragmentActivity implements
		OnClickListener {

	private Context mContext;

	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actionbtn)
	private ImageButton actionbtn;
	@ViewInject(R.id.detail_pager)
	private ViewPager viewpager;

	private Fragment productDetailFragment, serviceLogFragment;
	private List<Fragment> views;// Tab页面列表

	@ViewInject(R.id.updateservice)
	private Button updateservice;
	@ViewInject(R.id.menu_productdetail)
	private ImageButton menu_productdetail;
	@ViewInject(R.id.menu_serviecelog)
	private ImageView menu_serviecelog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		setContentView(R.layout.inservice_detial_activity);
		mContext = this;
		LogUtils.e(new Date().getTime() + "");
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("订单详情");
		actionbtn.setVisibility(View.GONE);
		views = new ArrayList<Fragment>();
		productDetailFragment = InServiceProductDetailFragment.getInstance();
		serviceLogFragment = ServiceLogFragment.getInstance();
		views.add(productDetailFragment);
		views.add(serviceLogFragment);
		viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewpager.setOnPageChangeListener(new PageChangeLisener());
		viewpager.setCurrentItem(0);

		updateservice.setOnClickListener(this);
		menu_productdetail.setOnClickListener(this);
		menu_serviecelog.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.menu_productdetail:
			viewpager.setCurrentItem(0);
			break;
		case R.id.menu_serviecelog:
			viewpager.setCurrentItem(1);
			break;
		case R.id.updateservice:
			intent = new Intent(InserviceDetailActivity.this,
					UpdateServiceActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			views = InserviceDetailActivity.this.views;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return views.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

	}

	class PageChangeLisener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				menu_productdetail
						.setBackgroundResource(R.drawable.productdetail_click);
				menu_serviecelog
						.setBackgroundResource(R.drawable.servicelog_def);
				break;
			case 1:
				menu_productdetail
						.setBackgroundResource(R.drawable.productdetail_def);
				menu_serviecelog
						.setBackgroundResource(R.drawable.servicelog_click);
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onStart() {
		LogUtils.e(new Date().getTime() + "");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogUtils.e(new Date().getTime() + "");
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
		super.onDestroy();
	}

}
