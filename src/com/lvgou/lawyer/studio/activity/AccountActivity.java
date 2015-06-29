package com.lvgou.lawyer.studio.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.adapter.TradingListAdapter;
import com.lvgou.lawyer.studio.adapter.WithdrawListAdapter;
import com.umeng.analytics.MobclickAgent;
/**
 * 资金账户
 * @author xiangyu
 *
 * @notes Created on 2014-9-16
 */
public class AccountActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.account_pager)
	private ViewPager viewpager;
	@ViewInject(R.id.tradinglist)
	private ListView tradinglist;
	@ViewInject(R.id.withdrawlist)
	private ListView withdrawlist;
	@ViewInject(R.id.menu_trading)
	private ImageButton menu_trading;
	@ViewInject(R.id.menu_withdraw)
	private ImageView menu_withdraw;
	@ViewInject(R.id.withdraw_btn)
	private ImageButton withdraw_btn;

	private TradingListAdapter tradingAdapter;
	private WithdrawListAdapter withdrawAdapter;
	private List<View> views;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

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
		setContentView(R.layout.account_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		titletext.setText("资金账户");
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		withdraw_btn.setOnClickListener(this);
		tradingAdapter = new TradingListAdapter(mContext, null);
		withdrawAdapter = new WithdrawListAdapter(mContext, null);
		tradinglist.setAdapter(tradingAdapter);
		withdrawlist.setAdapter(withdrawAdapter);
		views = new ArrayList<View>();
		views.add(tradinglist);
		views.add(withdrawlist);
		viewpager.setAdapter(new MyViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new PageChangeLisener());
		viewpager.setCurrentItem(0);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(AccountActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_trading:
			viewpager.setCurrentItem(0);
			break;
		case R.id.menu_withdraw:
			viewpager.setCurrentItem(1);
			break;
		case R.id.withdraw_btn:
			intent = new Intent(AccountActivity.this,
					AccountWithdrawActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	public class MyViewPagerAdapter extends PagerAdapter{
		private List<View> mListViews;
		
		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 	{	
			container.removeView(mListViews.get(position));//删除页卡
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡		
			 container.addView(mListViews.get(position), 0);//添加页卡
			 return mListViews.get(position);
		}

		@Override
		public int getCount() {			
			return  mListViews.size();//返回页卡的数量
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {			
			return arg0==arg1;//官方提示这样写
		}
	}
	
	private class PageChangeLisener implements OnPageChangeListener {

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
				menu_trading
						.setBackgroundResource(R.drawable.trading_record_btn_click);
				menu_withdraw
						.setBackgroundResource(R.drawable.withdraw_record_btn_def);
				break;
			case 1:
				menu_trading
						.setBackgroundResource(R.drawable.trading_record_btn_def);
				menu_withdraw
						.setBackgroundResource(R.drawable.withdraw_record_btn_click);
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

}
