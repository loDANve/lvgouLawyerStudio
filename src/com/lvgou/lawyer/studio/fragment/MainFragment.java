package com.lvgou.lawyer.studio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.lvgou.lawyer.studio.activity.FinishedActivity;
import com.lvgou.lawyer.studio.activity.InServiceActivity;
import com.lvgou.lawyer.studio.activity.IntakeActivity;
import com.lvgou.lawyer.studio.activity.NewOrderActivity;
import com.lvgou.lawyer.studio.activity.RefuseActivity;
import com.lvgou.lawyer.studio.activity.ToBeConfirmActivity;
import com.lvgou.lawyer.studio.been.Home;
import com.lvgou.lawyer.studio.been.SetSchedule;
import com.lvgou.lawyer.studio.utils.Utils;

/**
 * 主页Fragment
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-4-17
 */
public class MainFragment extends BaseFragment implements OnClickListener {
	private static MainFragment instance;

	private View v;

	/**
	 * 单例模式
	 * 
	 * @return fragment
	 */
	public static MainFragment getInstance() {
		if (instance == null) {
			instance = new MainFragment();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public MainFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@ViewInject(R.id.intake_btn)
	private ImageButton intake_btn;
	@ViewInject(R.id.neworder_btn)
	private RelativeLayout neworder_btn;
	@ViewInject(R.id.finished_btn)
	private ImageButton finished_btn;
	@ViewInject(R.id.inservice_btn)
	private ImageButton inservice_btn;
	@ViewInject(R.id.tobeconfirm_btn)
	private ImageButton tobeconfirm_btn;
	@ViewInject(R.id.refuse_btn)
	private ImageButton refuse_btn;
	@ViewInject(R.id.calendarstate_btn)
	private RelativeLayout calendarstate_btn;
	@ViewInject(R.id.calendarstate_img)
	private ImageView calendarstate_img;
	@ViewInject(R.id.neworder_count)
	private TextView neworder_count;
	@ViewInject(R.id.lastmonth)
	private TextView lastmonth;
	@ViewInject(R.id.allincome)
	private TextView allincome;
	@ViewInject(R.id.balance)
	private TextView balance;

	private boolean isfree = true;
	private HttpUtils http;
	private Home home;
	private SetSchedule ss;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				neworder_count.setText(home.getUnconfirmordernum());
				lastmonth.setText(home.getLastMonthIncome() + "");
				allincome.setText(home.getTotalIncome());
				// balance.setText(home.get)
				int schedule = Integer.parseInt(home.getSchedule());
				if (schedule == 0) {
					isfree = false;
					calendarstate_img
							.setBackgroundResource(R.drawable.main_isfree_no);
				} else {
					isfree = true;
					calendarstate_img
							.setBackgroundResource(R.drawable.main_isfree_yes);
				}
			default:

				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.main_content_fragment, container, false);
		ViewUtils.inject(this, v);
		intake_btn.setOnClickListener(this);
		neworder_btn.setOnClickListener(this);
		finished_btn.setOnClickListener(this);
		inservice_btn.setOnClickListener(this);
		tobeconfirm_btn.setOnClickListener(this);
		refuse_btn.setOnClickListener(this);
		calendarstate_btn.setOnClickListener(this);
		getHomeData();
		return v;
	}

	private void getHomeData() {
		home = new Home();
		home.setLawyerid(Utils.getLawyerid(getActivity()));
		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, home.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == getActivity()
								|| getActivity().isFinishing()) {
							return;
						}
						home = Utils.json2Object(
								responseInfo.result.getBytes(), home);
						// handler.sendEmptyMessage(lp.getCode());
						neworder_count.setText(home.getUnconfirmordernum());
						lastmonth.setText(home.getLastMonthIncome() + "");
						allincome.setText(home.getTotalIncome());
						// balance.setText(home.get)
						int schedule = Integer
								.parseInt(home.getSchedule() == null ? "0" : home
										.getSchedule());
						if (schedule == 0) {
							isfree = false;
							calendarstate_img
									.setBackgroundResource(R.drawable.main_isfree_no);
						} else {
							isfree = true;
							calendarstate_img
									.setBackgroundResource(R.drawable.main_isfree_yes);
						}
					}
				});
	}

	private void changeSchedule(final int schedule) {
		ss = new SetSchedule();
		ss.setLawyerid(Utils.getLawyerid(getActivity()));
		ss.setSetSchedule(schedule);
		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, ss.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == getActivity()
								|| getActivity().isFinishing()) {
							return;
						}
						ss = Utils.json2Object(responseInfo.result.getBytes(),
								ss);
						if (ss.getCode() == 1) {
							Toast.makeText(getActivity(), "操作成功",
									Toast.LENGTH_SHORT).show();
							if (schedule == 0) {
								isfree = false;
								calendarstate_img
										.setBackgroundResource(R.drawable.main_isfree_no);
							} else {
								isfree = true;
								calendarstate_img
										.setBackgroundResource(R.drawable.main_isfree_yes);
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.intake_btn:
			intent = new Intent(getActivity(), IntakeActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.neworder_btn:
			intent = new Intent(getActivity(), NewOrderActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.finished_btn:
			intent = new Intent(getActivity(), FinishedActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.inservice_btn:
			intent = new Intent(getActivity(), InServiceActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.tobeconfirm_btn:
			intent = new Intent(getActivity(), ToBeConfirmActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.refuse_btn:
			intent = new Intent(getActivity(), RefuseActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.calendarstate_btn:
			if (isfree) {
				changeSchedule(0);
			} else {
				changeSchedule(1);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
