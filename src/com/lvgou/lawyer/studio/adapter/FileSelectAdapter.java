package com.lvgou.lawyer.studio.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;

public class FileSelectAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	// private Bitmap back;
	private Context mContext;
	private Bitmap folder;
	private Bitmap file;
	private List<String> listdirs = null; // 文件夹列表
	private List<String> listfiles = null; // 文件列表
	private List<String> paths = null; // 全部列表
	private String rootPath = Environment.getExternalStorageDirectory()
			.getPath(); // SD卡根目录
	private String parentPath = rootPath; // 当前目录的上一级目录
	private Handler handler;

	private List<String> selected; // 选中文件路径集合

	public FileSelectAdapter(Context context, List<String> selected,
			Handler handler) {
		this.selected = selected;
		this.mContext = context;
		this.handler = handler;
		mInflater = LayoutInflater.from(mContext);
		// back = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.icon);
		folder = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.selectfile_def);
		file = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_icon);
		listdirs = new ArrayList<String>();
		listfiles = new ArrayList<String>();
		paths = new ArrayList<String>();
		selected = new ArrayList<String>();
		getFileDir(rootPath);
	}

	@Override
	public int getCount() {
		return paths.size();
	}

	@Override
	public Object getItem(int position) {
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fileselect_item, parent,
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

	public void getFileDir(String filePath) {
		paths.clear();
		listfiles.clear();
		listdirs.clear();
		if(filePath == null){
			filePath = rootPath;
		}
		File f = new File(filePath);
		File[] files = f.listFiles();
//		if (!filePath.equals(rootPath)) {
//			paths.add(rootPath);
//			if (!rootPath.equals(f.getParent())) {
//				paths.add(f.getParent());
//			}
//		}
		if (null != files) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					listdirs.add(file.getPath());
				} else {
					listfiles.add(file.getPath());
				}
			}
		}
		Collections.sort(listdirs); // 文件夹名称排序
		Collections.sort(listfiles);
		paths.addAll(listdirs); // 这里是为了保证文件夹在上面,文件在下面
		paths.addAll(listfiles);
		notifyDataSetChanged();
	}

	public class ViewHolder implements OnClickListener {
		@ViewInject(R.id.filename)
		private TextView filename;
		@ViewInject(R.id.fileicon)
		private ImageView fileicon;
		@ViewInject(R.id.select)
		private ImageView filecheck;
		@ViewInject(R.id.fileitem_rl)
		private RelativeLayout fileitem;

		private int position;
		private boolean ischecked = false;
		private File f;

		public ViewHolder(int position) {
			this.position = position;
		}

		public void update(int position) {
			this.position = position;
			refresh();
		}

		public void refresh() {
			// System.out.println(paths.get(position));
			// filecheck.setOnClickListener(this);
			fileitem.setOnClickListener(this);
			f = new File(paths.get(position));
			// if (paths.get(position).equals(rootPath)) {
			// filename.setText("返回根目录..");
			// fileicon.setImageBitmap(back);
			// filecheck.setVisibility(View.GONE);
			// } else if (paths.get(position).equals(parentPath)) {
			// filename.setText("返回上一层..");
			// fileicon.setImageBitmap(back);
			// filecheck.setVisibility(View.GONE);
			// } else {
			String fname = f.getName();
			if (fname.length() > 15) {
				fname = fname.substring(0, 15) + "...";
			}
			filename.setText(fname);
			if (f.isDirectory()) {
				fileicon.setImageBitmap(folder);
				filecheck.setVisibility(View.GONE);
			} else {
				fileicon.setImageBitmap(file);
				filecheck.setVisibility(View.VISIBLE);
				if (selected.contains(f.getPath())) {
					ischecked = true;
					filecheck
							.setBackgroundResource(R.drawable.umeng_update_btn_check_on_holo_light);
				} else {
					ischecked = false;
					filecheck
							.setBackgroundResource(R.drawable.umeng_update_btn_check_off_holo_light);
				}
			}
			// }
		}

		public void changeBg() {
			if (ischecked) {
				filecheck
						.setBackgroundResource(R.drawable.umeng_update_btn_check_off_holo_light);
				selected.remove(f.getPath());
			} else {
				filecheck
						.setBackgroundResource(R.drawable.umeng_update_btn_check_on_holo_light);
				selected.add(f.getPath());
			}
			ischecked = !ischecked;
		}
		
		private void addView(){
			Message msg = Message.obtain(handler, 2, f.getPath());
			handler.sendMessage(msg);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.fileitem_rl:
				if (f.isDirectory()) {
					parentPath = f.getParent();
					getFileDir(f.getPath());
					addView();
				} else {
					changeBg();
				}
				break;
			default:
				break;
			}
		}
	}
}