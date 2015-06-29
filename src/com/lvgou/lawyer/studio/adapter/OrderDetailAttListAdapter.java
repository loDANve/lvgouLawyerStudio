package com.lvgou.lawyer.studio.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.Intake;

public class OrderDetailAttListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Intake> list;

	public OrderDetailAttListAdapter(Context context, List<Intake> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
	}

	@Override
	public int getCount() {
//		return list.size();
		return 5;
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
			convertView = mInflater.inflate(R.layout.orderdetail_attlist_item, parent,
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

	private class ViewHolder{
		@ViewInject(R.id.filename)
		private TextView filename;
		@ViewInject(R.id.download_btn)
		private ImageButton download_btn;
		@ViewInject(R.id.download_progress)
		private ProgressBar download_progress;
		@ViewInject(R.id.download_progress_rl)
		private RelativeLayout download_progress_rl;
		
		private int position;
		
		public ViewHolder(int position) {
			this.position = position;
		}

		public void update(int position) {
			this.position = position;
			refresh();
		}
		
		public void refresh(){
			download_progress.setProgress(50);
			if(position < 3){
				download_progress_rl.setVisibility(View.GONE);
			}
		}
	} 
}