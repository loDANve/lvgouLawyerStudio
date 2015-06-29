package com.lvgou.lawyer.studio.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.Intake;

public class WithdrawListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Intake> list;

	public WithdrawListAdapter(Context context, List<Intake> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// return list.size();
		return 20;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.account_withdraw_item,
					parent, false);

		} else {

		}

		return convertView;
	}

}