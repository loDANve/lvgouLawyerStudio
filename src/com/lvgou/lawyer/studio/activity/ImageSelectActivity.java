package com.lvgou.lawyer.studio.activity;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.utils.ImageUtils;
import com.umeng.analytics.MobclickAgent;

public class ImageSelectActivity extends Activity implements
		OnClickListener {

	private Context mContext;
	final int RESULT_LOAD_IMAGE_PHOTO = 1;
	final int RESULT_LOAD_IMAGE_GRAPH = 2;
	private boolean hasImage = false;
	private String picturePath;
	final String imagename = "myimage.jpg";
	private File pictureFile;
	DisplayMetrics dm = null;
	private ImageUtils imageUtils;

	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.imageselet)
	private ImageView imageselet;
	@ViewInject(R.id.menu_rl)
	private RelativeLayout menu;
	@ViewInject(R.id.photo)
	private Button photo;
	@ViewInject(R.id.photograph)
	private Button photograph;
	@ViewInject(R.id.cancel)
	private Button cancel;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				BitmapDrawable bd = null;
				bd = new BitmapDrawable(imageUtils.zoomImg(500, 500,
						picturePath));
				if (bd != null) {
					imageselet.setBackground(bd);
				}
				hasImage = true;
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.imageselect_activity);
		mContext = this;
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlebar_main);
		ViewUtils.inject(this);
		imageUtils = new ImageUtils();
		initView();
	}

	private void initView() {
		titletext.setText("上传图片");
		imageselet.setOnClickListener(this);
		photo.setOnClickListener(this);
		photograph.setOnClickListener(this);
		cancel.setOnClickListener(this);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		menu.setVisibility(View.GONE);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case RESULT_LOAD_IMAGE_PHOTO:
				if (null != data) {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					picturePath = cursor.getString(columnIndex);
					pictureFile = new File(picturePath);
					cursor.close();
				}
				break;
			case RESULT_LOAD_IMAGE_GRAPH:
				if (requestCode == RESULT_LOAD_IMAGE_GRAPH) {
					if (pictureFile != null && pictureFile.exists()) {
						picturePath = pictureFile.getPath();
					}
				}
				break;
			default:
				break;
			}
			getImage();
		}
	}

	private void getImage() {
		System.out.println(picturePath);
		new Thread(new Runnable() {
			@Override
			public void run() {
//				imageUtils.saveImage(handler, pa, dm.widthPixels,
//						dm.heightPixels);
			}
		}).start();
	}

	private void goPhotoGraph() {
		// 先验证手机是否有sdcard
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Globals.FILEPATH);
				if (!dir.exists())
					dir.mkdirs();

				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

				pictureFile = new File(dir, imagename);// localTempImgDir和localTempImageFileName是自己定义的名字
				if (pictureFile.exists()) {
					pictureFile.delete();
				}
				Uri u = Uri.fromFile(pictureFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, RESULT_LOAD_IMAGE_GRAPH);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(ImageSelectActivity.this, "没有找到储存目录",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(ImageSelectActivity.this, "没有储存卡", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void cancelMenu() {
		TranslateAnimation trans = new TranslateAnimation(0, 0, 0,
				dm.heightPixels);
		trans.setDuration(500);
		menu.setAnimation(trans);
		menu.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		TranslateAnimation trans = null;
		Intent intent = null;
		switch (v.getId()) {
		case R.id.photo:
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, RESULT_LOAD_IMAGE_PHOTO);
			break;
		case R.id.photograph:
			goPhotoGraph();
			break;
		case R.id.cancel:
			cancelMenu();
			break;
		case R.id.imageselet:
			if (!hasImage) {
				menu.getBackground().setAlpha(80);
				trans = new TranslateAnimation(0, 0, dm.heightPixels, 0);
				trans.setDuration(500);
				menu.setAnimation(trans);
				menu.setVisibility(View.VISIBLE);
			} else {
				intent = new Intent(ImageSelectActivity.this,
						ImageShowActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
