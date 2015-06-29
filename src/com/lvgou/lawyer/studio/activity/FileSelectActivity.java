package com.lvgou.lawyer.studio.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.adapter.FileSelectAdapter;

public class FileSelectActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.filelist)
	private ListView filelist;
	@ViewInject(R.id.mPath)
	private TextView mPath;
	@ViewInject(R.id.title_ll)
	private LinearLayout title_ll;
	@ViewInject(R.id.title_hsv)
	private HorizontalScrollView title_hsv;

	private Context mContext;
	private List<String> selected; // 选择的文件路径
	private FileSelectAdapter adapter; // 文件列表适配器
	private List<String> titlePaths;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = null;
			if (msg.obj == null) {
				return;
			}
			switch (msg.what) {
			case 1:

				break;
			case 2:
				addView(msg.obj.toString());
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fileselect_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_main);
		mContext = this;
		ViewUtils.inject(this);
		titlePaths = new ArrayList<String>();
		initView();
	}

	private void initView() {
		titletext.setText("选择文件");
		actiontext.setText("完成");
		mPath.setText("手机");
		mPath.setOnClickListener(this);
		actiontext.setOnClickListener(this);
		back_img.setVisibility(View.VISIBLE);
		back_img.setOnClickListener(this);
		selected = new ArrayList<String>();
		adapter = new FileSelectAdapter(mContext, selected, handler);
		filelist.setAdapter(adapter);
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		filelist.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true)); // 防止滚动卡屏
	}

	private void addView(String path) {
		titlePaths.add(path);
		int index = path.lastIndexOf("/");
		String name = path.substring(index + 1);
		TextView text = new TextView(mContext);
		text.setTextAppearance(mContext, R.style.DetailTitleStyle);
		text.setText(name);
		title_ll.addView(text);
		final int viewIndex = title_ll.indexOfChild(text);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title_ll.removeViews(viewIndex + 1, title_ll.getChildCount()
						- viewIndex - 1);
				adapter.getFileDir(titlePaths.get(viewIndex - 1));
			}
		});
		System.out.println(title_ll.indexOfChild(text) + "");
		TextView view = (TextView) title_ll.getChildAt(title_ll
				.indexOfChild(text) - 1);
		view.setText(view.getText().toString().replace(" > ", "") + " > ");
		title_hsv.scrollTo(title_ll.getMeasuredWidth(), 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actiontext:
			// 确认按钮点击,将已选择文件传给上级activity
			Intent data = new Intent(FileSelectActivity.this,
					UploadActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("paths", (Serializable) selected);
			data.putExtras(bundle);
			setResult(1, data);
			finish();
			break;
		case R.id.mPath:
			title_ll.removeViews(1, title_ll.getChildCount() - 1);
			adapter.getFileDir(null);
			break;
		// case R.id.back_img:
		// //清空按钮,清除已选
		// selected.clear();
		// adapter.notifyDataSetChanged();
		// break;
		default:
			break;
		}
	}

}
