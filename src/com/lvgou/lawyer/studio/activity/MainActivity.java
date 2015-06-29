package com.lvgou.lawyer.studio.activity;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.fragment.BBSFragment;
import com.lvgou.lawyer.studio.fragment.MainFragment;
import com.lvgou.lawyer.studio.fragment.MyCenterFragment;
import com.lvgou.lawyer.studio.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private Context mContext;

	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.actionbtn)
	private ImageButton actionbtn;
	@ViewInject(R.id.viewpager)
	private ViewPager viewpager;

	private Fragment contentFragment, mainFragment, bbsFragment,
			myCenterFragment;
	private List<Fragment> views;// Tab页面列表

	@ViewInject(R.id.menu_main_img)
	private ImageView menu_main_img;
	@ViewInject(R.id.menu_bbs_img)
	private ImageView menu_bbs_img;
	@ViewInject(R.id.menu_mycenter_img)
	private ImageView menu_mycenter_img;

	// 用于切换菜单按钮背景图
	private ImageView[] imageViews;
	private int[] defBgs; // 默认背景
	private int[] clickedBgs; // 被选中背景
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		if (!Utils.hasBind(mContext)) {
			// 绑定百度云推送
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					Utils.getMetaValue(getApplicationContext(), "api_key"));
		}
		initView();
	}

	private void initView() {
		titletext.setText("首页");
		actionbtn.setBackgroundResource(R.drawable.account_btn_setting);
		actionbtn.setOnClickListener(this);
		views = new ArrayList<Fragment>();
		mainFragment = MainFragment.getInstance();
		bbsFragment = BBSFragment.getInstance();
		myCenterFragment = MyCenterFragment.getInstance();
		views.add(mainFragment);
		views.add(bbsFragment);
		views.add(myCenterFragment);
		viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewpager.setOnPageChangeListener(new PageChangeLisener());
		viewpager.setCurrentItem(0);
		contentFragment = mainFragment;

		menu_main_img.setOnClickListener(this);
		menu_bbs_img.setOnClickListener(this);
		menu_mycenter_img.setOnClickListener(this);
		imageViews = new ImageView[] { menu_main_img, menu_bbs_img,
				menu_mycenter_img };
		defBgs = new int[] { R.drawable.menu_main_def, R.drawable.menu_bbs_def,
				R.drawable.menu_mycenter_def };
		clickedBgs = new int[] { R.drawable.menu_main_click, R.drawable.menu_bbs_click,
				R.drawable.menu_mycenter_click };
	}

	private void changeMenuBg(int selected) {
		if (imageViews == null || defBgs == null || clickedBgs == null) {
			return;
		}
		if (selected > imageViews.length || selected > defBgs.length
				|| selected > clickedBgs.length) {
			return;
		}
		for (int i = 0, s = imageViews.length; i < s; i++) {
			if (i == (selected - 1)) {
				imageViews[i].setBackgroundResource(clickedBgs[i]);
			} else {
				imageViews[i].setBackgroundResource(defBgs[i]);
			}
		}

	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.menu_main_img:
			viewpager.setCurrentItem(0);
			break;
		case R.id.menu_bbs_img:
			viewpager.setCurrentItem(1);
			break;
		case R.id.menu_mycenter_img:
			viewpager.setCurrentItem(2);
			break;
		case R.id.actionbtn:
			intent = new Intent(mContext,SettingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "contentFragment",
				contentFragment);
	}

	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			views = MainActivity.this.views;
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
			changeMenuBg(arg0 + 1);
			contentFragment = views.get(arg0);
			switch (arg0) {
			case 0:
				titletext.setText("首页");
				action_rl.setVisibility(View.GONE);
				break;
			case 1:
				titletext.setText("互动中心");
				action_rl.setVisibility(View.GONE);
				break;
			case 2:
				titletext.setText("个人中心");
				action_rl.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
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
		super.onDestroy();
	}

}
