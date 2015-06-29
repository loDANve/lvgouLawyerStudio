package com.lvgou.lawyer.studio.activity;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.Globals;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.been.City;
import com.lvgou.lawyer.studio.been.UploadPhoto;
import com.umeng.analytics.MobclickAgent;

public class IdentificationActivity extends Activity implements OnClickListener {

	private Context mContext;
	@ViewInject(R.id.titletext)
	private TextView titletext;
	@ViewInject(R.id.actiontext)
	private TextView actiontext;
	@ViewInject(R.id.back_img)
	private ImageButton back_img;
	@ViewInject(R.id.hint_text)
	private TextView hint_text;
	@ViewInject(R.id.action_rl)
	private RelativeLayout action_rl;
	@ViewInject(R.id.hint_img)
	private ImageView hint_img;
	@ViewInject(R.id.hint_ll)
	private LinearLayout hint_ll;
	@ViewInject(R.id.service_area_ll)
	private LinearLayout service_area_ll;
	@ViewInject(R.id.service_selected)
	private TextView service_selected;
	@ViewInject(R.id.location_area_ll)
	private LinearLayout location_area_ll;
	@ViewInject(R.id.location_selected)
	private TextView location_selected;
	@ViewInject(R.id.menu_rl)
	private RelativeLayout menu;
	@ViewInject(R.id.photo)
	private Button photo;
	@ViewInject(R.id.photograph)
	private Button photograph;
	@ViewInject(R.id.cancel)
	private Button cancel;
	@ViewInject(R.id.credential_img_select_rl)
	private RelativeLayout credential_img_select_rl;
	@ViewInject(R.id.photo_select_rl)
	private RelativeLayout photo_select_rl;
	@ViewInject(R.id.credential_img)
	private ImageView credential_img;
	@ViewInject(R.id.photo_img)
	private ImageView photo_img;

	private final int LOCATION_AREA_RESULT_CODE = 1;
	private final int SERVICE_AREA_RESULT_CODE = 2;
	private final int RESULT_LOAD_IMAGE_PHOTO = 3;
	private final int RESULT_LOAD_IMAGE_GRAPH = 4;
	private List<City> selectedlist;
	private String picturePath_credential;
	private String picturePath_photo;
	private String picturePath;
	private String imagename;
	private File pictureFile;
	private DisplayMetrics dm = null;
	private int imageType = 0; // 1:执业证书照，2:一寸照片
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (picturePath == null) {
					Toast.makeText(mContext, "获取图片失败，请重新拍摄", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				BitmapUtils bitmaputils = new BitmapUtils(mContext);
				bitmaputils.closeCache();
				BitmapDisplayConfig config = new BitmapDisplayConfig();
				config.setAutoRotation(true);
				BitmapSize size = new BitmapSize((int) (65 * dm.density),
						(int) (65 * dm.density));
				config.setBitmapMaxSize(size);
				if (imageType == 1) {
					bitmaputils.display(credential_img, picturePath, config,
							new ImageCallBack());
				} else {
					bitmaputils.display(photo_img, picturePath, config,
							new ImageCallBack());
				}
				// BitmapDrawable bd = null;
				// Bitmap bm = imageUtils.zoomImg((int) (65 * dm.density),
				// (int) (65 * dm.density), picturePath);
				// bd = new BitmapDrawable(getResources(), bm);
				// if (bd != null) {
				// if (imageType == 1) {
				// credential_img.setBackground(bd);
				// LogUtils.e(imageUtils.readPictureDegree(picturePath)
				// + "");
				// credential_img.setRotation(imageUtils
				// .readPictureDegree(picturePath));
				// } else {
				// photo_img.setBackground(bd);
				// photo_img.setRotation(imageUtils
				// .readPictureDegree(picturePath));
				// }
				// }
				break;
			case 1002:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("手机号不能为空");
				hint_ll.setVisibility(View.VISIBLE);
				break;

			case 1003:
				hint_img.setBackgroundResource(R.drawable.hint_img_false);
				hint_text.setText("验证码不能为空");
				hint_ll.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identification_activity);
		mContext = this;
		LogUtils.e(new Date() + "");
		ViewUtils.inject(this);
		selectedlist = new ArrayList<City>();
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		initView();
	}

	private void initView() {
		titletext.setText("律师认证");
		actiontext.setText("提交资料");
		action_rl.setVisibility(View.VISIBLE);
		// back_rl.setOnClickListener(this);
		// back_rl.setVisibility(View.VISIBLE);
		// back_rl.setOnClickListener(this);
		service_area_ll.setOnClickListener(this);
		credential_img_select_rl.setOnClickListener(this);
		photo_select_rl.setOnClickListener(this);
		photograph.setOnClickListener(this);
		cancel.setOnClickListener(this);
		photo.setOnClickListener(this);
		credential_img.setOnClickListener(this);
		photo_img.setOnClickListener(this);
		location_area_ll.setOnClickListener(this);
		action_rl.setOnClickListener(this);
		menu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		menu.setVisibility(View.GONE);
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = null;
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
				getImage();
				break;
			case RESULT_LOAD_IMAGE_GRAPH:
				if (pictureFile != null && pictureFile.exists()) {
					picturePath = pictureFile.getPath();
				}
				getImage();
				break;
			case SERVICE_AREA_RESULT_CODE:

				if (data != null && (bundle = data.getExtras()) != null) {
					selectedlist = (List<City>) bundle.getSerializable("citys");
				}
				if (selectedlist != null) {
					StringBuffer sbf = new StringBuffer();
					for (City city : selectedlist) {
						LogUtils.e(city.getName());
						sbf.append(city.getName() + ",");
					}
					if (sbf.length() > 0) {
						service_selected.setText(sbf.toString().substring(0,
								sbf.length() - 1));
					}
				} else {
					service_selected.setText("请选择您可服务的地区");
				}
				break;
			case LOCATION_AREA_RESULT_CODE:
				City city = null;
				if (data != null && (bundle = data.getExtras()) != null) {
					city = (City) bundle.getSerializable("city");
				}
				if (city != null) {
					location_selected.setText(city.getName() == null ? ""
							: city.getName());
				} else {
					location_selected.setText("请选择您可服务的地区");
				}
				break;
			default:
				break;
			}
		}
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
				imagename = new Date().getTime() + "";
				pictureFile = new File(dir, imagename);// localTempImgDir和localTempImageFileName是自己定义的名字
				if (pictureFile.exists()) {
					pictureFile.delete();
				}
				Uri u = Uri.fromFile(pictureFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, RESULT_LOAD_IMAGE_GRAPH);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(IdentificationActivity.this, "没有找到储存目录",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(IdentificationActivity.this, "没有储存卡",
					Toast.LENGTH_LONG).show();
		}
	}

	private void getImage() {
		LogUtils.e(picturePath + "");
		if (imageType == 1) {
			picturePath_credential = picturePath;
		} else {
			picturePath_photo = picturePath;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// imageUtils.saveImage(handler, imageType, picturePath,
				// dm.widthPixels, dm.heightPixels);
			}
		}).start();
		handler.sendEmptyMessage(1);
	}

	private void cancelMenu() {
		TranslateAnimation trans = new TranslateAnimation(0, 0, 0,
				dm.heightPixels);
		trans.setDuration(500);
		menu.setAnimation(trans);
		menu.setVisibility(View.GONE);
	}

	private void showMenu() {
		TranslateAnimation trans = null;
		menu.getBackground().setAlpha(80);
		trans = new TranslateAnimation(0, 0, dm.heightPixels, 0);
		trans.setDuration(500);
		menu.setAnimation(trans);
		menu.setVisibility(View.VISIBLE);
	}

	private void goShowImg(String path) {
		Intent intent = new Intent(IdentificationActivity.this,
				ImageShowActivity.class);
		Bundle bundel = new Bundle();
		LogUtils.e(path + "");
		bundel.putString("path", path);
		intent.putExtras(bundel);
		startActivity(intent);
	}

	private void doUploadPhoto() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", new File(picturePath_photo));
		UploadPhoto up = new UploadPhoto();
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, up.getUrl(), params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						Toast.makeText(mContext, "开始上传！", Toast.LENGTH_SHORT)
								.show();
						LogUtils.d("uploadStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							LogUtils.e("upload: " + current + "/" + total);
						} else {
							LogUtils.e("reply: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.e("reply: " + responseInfo.result);
						location_selected.setText(responseInfo.result);
						Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.e(error.getExceptionCode() + ":" + msg);
						location_selected.setText(error.getExceptionCode() + ":" + msg);
						Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	private void doUploadPhotoToCache() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", new File(picturePath_photo));
//		UploadPhoto up = new UploadPhoto();
		HttpUtils http = new HttpUtils();
		LogUtils.e(Globals.lvgouAPI + "passport/upload");
		http.send(HttpRequest.HttpMethod.POST, Globals.lvgouAPI + "passport/upload", params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						Toast.makeText(mContext, "开始上传！", Toast.LENGTH_SHORT)
								.show();
						LogUtils.d("uploadStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							LogUtils.e("upload: " + current + "/" + total);
						} else {
							LogUtils.e("reply: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.e("reply: " + responseInfo.result);
						location_selected.setText(responseInfo.result);
						Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.e(error.getExceptionCode() + ":" + msg);
						location_selected.setText(error.getExceptionCode() + ":" + msg);
						Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.finish:
			// intent = new Intent(EditPwdActivity.this,
			// LoginPwdActivity.class);
			// startActivity(intent);
			finish();
			break;
		case R.id.service_area_ll:
			intent = new Intent(IdentificationActivity.this,
					CitySelectActivity.class);
			bundle = new Bundle();
			bundle.putInt("selectType", 2);
			bundle.putSerializable("citys", (Serializable) selectedlist);
			intent.putExtras(bundle);
			startActivityForResult(intent, SERVICE_AREA_RESULT_CODE);
			break;
		case R.id.location_area_ll:
			intent = new Intent(IdentificationActivity.this,
					CitySelectActivity.class);
			bundle = new Bundle();
			bundle.putInt("selectType", 1);
			intent.putExtras(bundle);
			startActivityForResult(intent, LOCATION_AREA_RESULT_CODE);
			break;
		case R.id.photo_select_rl:
			imageType = 2;
			showMenu();
			break;
		case R.id.credential_img_select_rl:
			imageType = 1;
			showMenu();
			break;
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
		case R.id.photo_img:
			goShowImg(picturePath_photo);
			break;
		case R.id.credential_img:
			goShowImg(picturePath_credential);
			break;
		case R.id.action_rl:
			// intent = new Intent(IdentificationActivity.this,
			// IdentificationStateActivity.class);
			// startActivity(intent);
			// finish();
			doUploadPhoto();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (menu.getVisibility() != View.GONE) {
				cancelMenu();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class ImageCallBack extends BitmapLoadCallBack<View> {

		@Override
		public void onLoadCompleted(View arg0, String arg1, Bitmap arg2,
				BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
			LogUtils.e(arg1 + "");
			arg0.setBackground(new BitmapDrawable(getResources(), arg2));
			Toast.makeText(mContext, "图片载入成功", Toast.LENGTH_SHORT).show();
			doUploadPhotoToCache();
		}

		@Override
		public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
			LogUtils.e(arg1 + "");
			Toast.makeText(mContext, "图片载入失败，请重新选择", Toast.LENGTH_SHORT).show();
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

}
