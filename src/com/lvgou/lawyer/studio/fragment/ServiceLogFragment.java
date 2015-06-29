package com.lvgou.lawyer.studio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.activity.IntakeActivity;
import com.lvgou.lawyer.studio.adapter.ServiceLogListAdapter;

/**
 * 主页Fragment
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-4-17
 */
public class ServiceLogFragment extends BaseFragment implements OnClickListener {
	private static ServiceLogFragment instance;

	private View v;

	/**
	 * 单例模式
	 * 
	 * @return fragment
	 */
	public static ServiceLogFragment getInstance() {
		if (instance == null) {
			instance = new ServiceLogFragment();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public ServiceLogFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@ViewInject(R.id.serviceloglist)
	private ListView serviceloglist;

	private ServiceLogListAdapter alogAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.servicelog_fragment, container, false);
		ViewUtils.inject(this, v);
		alogAdapter = new ServiceLogListAdapter(getActivity(), null);
		serviceloglist.setAdapter(alogAdapter);

		serviceloglist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					alogAdapter.setCanSet(true);
					alogAdapter.notifyDataSetChanged();
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					alogAdapter.setCanSet(false);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					alogAdapter.setCanSet(false);
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		}); // 防止滚动卡屏
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.intake_btn:
			intent = new Intent(getActivity(), IntakeActivity.class);
			getActivity().startActivity(intent);
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
