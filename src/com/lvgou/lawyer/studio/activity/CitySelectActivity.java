package com.lvgou.lawyer.studio.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.adapter.CityListAdapter;
import com.lvgou.lawyer.studio.been.City;
import com.lvgou.lawyer.studio.db.CityDBHelper;
import com.lvgou.lawyer.studio.view.MyLetterListView;
import com.lvgou.lawyer.studio.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.umeng.analytics.MobclickAgent;

public class CitySelectActivity extends Activity implements OnClickListener {

	private Context mContext;
	private List<City> allCity_lists; // 所有城市列表
	private List<City> selectedlist;
	private String[] sections;// 存放存在的汉语拼音首字母
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private OverlayThread overlayThread; // 显示首字母对话框
	private CityListAdapter adapter;

	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.citylistview)
	private ListView cityList;
	private MyLetterListView letterListView;
	private TextView overlay; // 对话框首字母textview
	private int selectType;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = null;
			switch (msg.what) {
			case 1:

				break;
			case 2:
				if (null != selectedlist) {
					titletext.setText("地区选择(" + selectedlist.size() + "/5)");
				} else {
					titletext.setText("地区选择(0/5)");
				}
				break;
			case 3:
				City city = (City) msg.obj;
				intent = new Intent(CitySelectActivity.this,
						IdentificationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("city", city);
				intent.putExtras(bundle);
				setResult(-1, intent);
				finish();
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
		setContentView(R.layout.cityselect_activity);
		mContext = this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		ViewUtils.inject(this);
		LogUtils.e(new Date().getTime() + "");
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			selectedlist = (List<City>) bundle.getSerializable("citys");
			selectType = bundle.getInt("selectType");
		}
		initView();
	}

	private void initView() {
		if (selectedlist == null) {
			selectedlist = new ArrayList<City>();
		}
		if (selectType == 1) {
			titletext.setText("地区选择");
		} else {
			handler.sendEmptyMessage(2);
		}
		actiontext.setText("完成");
		action_rl.setOnClickListener(this);
		action_rl.setVisibility(View.VISIBLE);
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		allCity_lists = new ArrayList<City>();
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		overlayThread = new OverlayThread();
		cityList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});

		initOverlay();
		hotCityInit();
		sections = new String[allCity_lists.size()];
		adapter = new CityListAdapter(mContext, allCity_lists, alphaIndexer,
				sections, selectedlist, handler, selectType);
		cityList.setAdapter(adapter);
	}

	/**
	 * 热门城市
	 */
	public void hotCityInit() {
		if (selectType == 1) {
			City city = new City();
			city.setName("search");
			allCity_lists.add(city);
		}
		// allCity_lists.add(city);
		// city = new City("", "#");
		// allCity_lists.add(city);
		// city = new City("上海", "");
		// allCity_lists.add(city);
		// city = new City("北京", "");
		// allCity_lists.add(city);
		// city = new City("广州", "");
		// allCity_lists.add(city);
		// city = new City("深圳", "");
		// allCity_lists.add(city);
		// city = new City("武汉", "");
		// allCity_lists.add(city);
		// city = new City("天津", "");
		// allCity_lists.add(city);
		// city = new City("西安", "");
		// allCity_lists.add(city);
		// city = new City("南京", "");
		// allCity_lists.add(city);
		// city = new City("杭州", "");
		// allCity_lists.add(city);
		// city = new City("成都", "");
		// allCity_lists.add(city);
		// city = new City("重庆", "");
		// allCity_lists.add(city);
		allCity_lists.addAll(getCityList());
	}

	private ArrayList<City> getCityList() {
		CityDBHelper dbHelper = new CityDBHelper(this);
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * a-z排序
	 */
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}

		}
	};

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		if (overlay != null) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater
				.inflate(R.layout.cityselect_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				cityList.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.action_rl:
			intent = new Intent(CitySelectActivity.this,
					IdentificationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("citys", (Serializable) selectedlist);
			intent.putExtras(bundle);
			setResult(-1, intent);
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
	protected void onStart() {
		LogUtils.e(new Date().getTime() + "");
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeView(overlay);
	}

}
