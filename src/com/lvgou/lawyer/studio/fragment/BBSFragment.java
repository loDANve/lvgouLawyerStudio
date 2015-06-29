package com.lvgou.lawyer.studio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.activity.BBSDetailActivity;
import com.lvgou.lawyer.studio.adapter.BBSListAdapter;

/**
 * 主页Fragment
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-4-17
 */
public class BBSFragment extends BaseFragment implements OnClickListener {
	private static BBSFragment instance;

	/**
	 * 单例模式
	 * 
	 * @return fragment
	 */
	public static BBSFragment getInstance() {
		if (instance == null) {
			instance = new BBSFragment();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public BBSFragment() {
		super();
	}

	@ViewInject(R.id.allpostlist)
	private ListView allpostlist;
	@ViewInject(R.id.mypostlist)
	private ListView mypostlist;
	@ViewInject(R.id.menu_allpost)
	private ImageButton menu_allpost;
	@ViewInject(R.id.menu_mypost)
	private ImageButton menu_mypost;

	private BBSListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_bbs_fragment, container, false);
		ViewUtils.inject(this, v);
		adapter = new BBSListAdapter(getActivity(), null);
		menu_allpost.setOnClickListener(this);
		menu_mypost.setOnClickListener(this);
		allpostlist.setAdapter(adapter);
		allpostlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						BBSDetailActivity.class);
				getActivity().startActivity(intent);
				intent = null;
			}
		});
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_allpost:
			menu_allpost
					.setBackgroundResource(R.drawable.bbs_menu_allpost_click);
			menu_mypost.setBackgroundResource(R.drawable.bbs_menu_mypost_def);
			break;
		case R.id.menu_mypost:
			menu_allpost.setBackgroundResource(R.drawable.bbs_menu_allpost_def);
			menu_mypost.setBackgroundResource(R.drawable.bbs_menu_mypost_click);
			break;
		default:
			break;
		}

	}
}
