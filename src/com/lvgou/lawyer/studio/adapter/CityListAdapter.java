package com.lvgou.lawyer.studio.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.City;

public class CityListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<City> list;
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private List<City> selectedlist;
	private Context mContext;
	final int VIEW_TYPE = 2;
	private int selectType;

	public CityListAdapter(Context context, List<City> list,
			HashMap<String, Integer> alphaIndexer, String[] sections,
			List<City> selectedlist, Handler handler, int selectType) {
		this.selectedlist = selectedlist;
		this.inflater = LayoutInflater.from(context);
		this.handler = handler;
		this.sections = sections;
		this.mContext = context;
		this.list = list;
		this.selectType = selectType;
		for (int i = 0; i < list.size(); i++) {
			// 当前汉语拼音首字母
			String currentStr = getAlpha(list.get(i).getPinyi());
			// 上一个汉语拼音首字母，如果不存在为“ ”
			String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
					.getPinyi()) : " ";
			if (!previewStr.equals(currentStr)) {
				String name = getAlpha(list.get(i).getPinyi());
				alphaIndexer.put(name, i);
				sections[i] = name;
			}
		}
	}

	@Override
	public int getCount() {
		return list.size();
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type = 1;
		if (position == 0) {
			City city = list.get(position);
			if (city.getName().equals("search")) {
				type = 0;
			}
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
		return VIEW_TYPE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		SearchHolder searchHolder;
		int viewType = getItemViewType(position);
		if (viewType == 0) {
			if (convertView == null) {
				searchHolder = new SearchHolder();
				convertView = inflater.inflate(
						R.layout.cityselect_citylist_item_search, null);
				searchHolder.search_edit = (EditText) convertView
						.findViewById(R.id.search);
				searchHolder.search_img = (ImageView) convertView
						.findViewById(R.id.search_img);
				convertView.setTag(searchHolder);
			} else {
				searchHolder = (SearchHolder) convertView.getTag();
			}
			searchHolder.search_img.setOnClickListener(new searchClick());
			searchHolder.search_edit.setOnClickListener(new searchClick());

		} else {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.cityselect_citylist_item, null);
				holder = new ViewHolder(position);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
				holder.refresh();
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.update(position);
			}
		}

		return convertView;
	}

	private class searchClick implements OnClickListener {

		@Override
		public void onClick(View v) {

		}

	}

	private class SearchHolder {
		@ViewInject(R.id.search)
		private EditText search_edit;
		@ViewInject(R.id.search_img)
		private ImageView search_img;
	}

	private class ViewHolder {
		@ViewInject(R.id.alpha)
		private TextView alpha; // 首字母标题
		@ViewInject(R.id.name)
		private TextView name; // 城市名字
		@ViewInject(R.id.selected_img)
		private ImageView selected_img; // 选中图标
		@ViewInject(R.id.cityname_ll)
		private RelativeLayout cityname_ll;

		private int position;
		private boolean ischecked;

		public ViewHolder(int position) {
			this.position = position;
		}

		public void update(int position) {
			this.position = position;
			refresh();
		}

		public void refresh() {
			ischecked = false;
			name.setText(list.get(position).getName());
			String currentStr = getAlpha(list.get(position).getPinyi());
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
					position - 1).getPinyi()) : " ";
			if (!previewStr.equals(currentStr)) {
				alpha.setVisibility(View.VISIBLE);
				if (currentStr.equals("#")) {
					currentStr = "热门城市";
				}
				alpha.setText(currentStr);
			} else {
				alpha.setVisibility(View.GONE);
			}

			if (selectType == 1) {
				cityname_ll.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Message msg = new Message();
						msg.obj = list.get(position);
						msg.what = 3;
						handler.sendMessage(msg);
					}
				});
			} else {
				for (City city : selectedlist) {
					if (city.getName().equals(list.get(position).getName())) {
						ischecked = true;
					}
				}

				if (ischecked) {
					selected_img.setVisibility(View.VISIBLE);
				} else {
					selected_img.setVisibility(View.INVISIBLE);
				}

				cityname_ll.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ischecked) {
							selected_img.setVisibility(View.INVISIBLE);
							for (City city : selectedlist) {
								if (city.getName().equals(
										list.get(position).getName())) {
									selectedlist.remove(city);
									break;
								}
							}
						} else {
							if (selectedlist.size() >= 5) {
								Toast.makeText(mContext, "最多只能选5个",
										Toast.LENGTH_SHORT).show();
							} else {
								selected_img.setVisibility(View.VISIBLE);
								selectedlist.add(list.get(position));
							}

						}
						ischecked = !ischecked;
						handler.sendEmptyMessage(2);
					}
				});
			}
		}
	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.equals("-")) {
			return "&";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		if (str.equals("search")) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
}