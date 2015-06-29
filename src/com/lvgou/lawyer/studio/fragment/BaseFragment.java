package com.lvgou.lawyer.studio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lvgou.lawyer.studio.activity.MainActivity;

/**
 * Fragment基类
 * @author xiangyu
 *
 * @notes Created on 2014-4-17
 */
public class BaseFragment extends Fragment {

	/**
	 * 构造方法
	 */
	public BaseFragment() {
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	protected void switchFragment(Fragment fragment, int animType) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity main = (MainActivity) getActivity();
//			main.switchContent(fragment, animType);
		}
	}

}
