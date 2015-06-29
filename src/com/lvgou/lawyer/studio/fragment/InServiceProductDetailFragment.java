package com.lvgou.lawyer.studio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.activity.FinishedActivity;
import com.lvgou.lawyer.studio.activity.InServiceActivity;
import com.lvgou.lawyer.studio.activity.IntakeActivity;
import com.lvgou.lawyer.studio.activity.NewOrderActivity;
import com.lvgou.lawyer.studio.activity.RefuseActivity;
import com.lvgou.lawyer.studio.activity.ToBeConfirmActivity;
import com.lvgou.lawyer.studio.adapter.OrderDetailAttListAdapter;
import com.lvgou.lawyer.studio.utils.Utils;

/**
 * 主页Fragment
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-4-17
 */
public class InServiceProductDetailFragment extends BaseFragment implements
		OnClickListener {
	private static InServiceProductDetailFragment instance;

	private View v;

	/**
	 * 单例模式
	 * 
	 * @return fragment
	 */
	public static InServiceProductDetailFragment getInstance() {
		if (instance == null) {
			instance = new InServiceProductDetailFragment();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public InServiceProductDetailFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@ViewInject(R.id.attlist)
	private ListView attlist;

	private OrderDetailAttListAdapter attAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.inservice_productdetail_fragment,
				container, false);
		ViewUtils.inject(this, v);
		attAdapter = new OrderDetailAttListAdapter(getActivity(), null);
		attlist.setAdapter(attAdapter);
		Utils.setListViewHeightBasedOnChildren(attlist);
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
