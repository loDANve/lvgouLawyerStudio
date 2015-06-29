package com.lvgou.lawyer.studio.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.Intake;
import com.lvgou.lawyer.studio.utils.Utils;

public class ServiceLogListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Intake> list;
	private OrderDetailAttListAdapter attadapter;
	private boolean canSet = true;

	public ServiceLogListAdapter(Context context, List<Intake> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// return list.size();
		return 5;
	}

	public void setCanSet(boolean canSet) {
		this.canSet = canSet;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.servicelog_item, parent,
					false);
			holder = new ViewHolder(position);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
			holder.refresh();
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.update(position);
		}

		return convertView;
	}

	private class ViewHolder {
		@ViewInject(R.id.attlist)
		private ListView attlist;
		@ViewInject(R.id.attlist_user)
		private ListView attlist_user;

		private int position;

		public ViewHolder(int position) {
			this.position = position;
		}

		public void update(int position) {
			this.position = position;
			refresh();
		}

		public void refresh() {
//			ViewGroup.LayoutParams params = attlist.getLayoutParams();
//			params.height = 5*30*2;
//			attlist.setLayoutParams(params);
//			attlist_user.setLayoutParams(params);
			if (canSet) {
				attadapter = new OrderDetailAttListAdapter(mContext, null);
				attlist.setAdapter(attadapter);
				attadapter = new OrderDetailAttListAdapter(mContext, null);
				attlist_user.setAdapter(attadapter);
				System.out.println("canSet  :  " + canSet);
				Utils.setListViewHeightBasedOnChildren(attlist);
				Utils.setListViewHeightBasedOnChildren(attlist_user);
			}
		}
	}
}