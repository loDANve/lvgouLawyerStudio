package com.lvgou.lawyer.studio.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;

public class UploadActivity extends Activity implements OnClickListener {
	public static final int FILE_RESULT_CODE = 1;
	@ViewInject(R.id.selectfile)
	private Button selectfile;
	@ViewInject(R.id.filetext)
	private TextView filetext;
	
	private List<String> paths = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_activity);
		ViewUtils.inject(this);
		initView();
	}
	
	private void initView(){
		selectfile.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectfile:
			Intent intent = new Intent(UploadActivity.this,
					FileSelectActivity.class);
			startActivityForResult(intent, FILE_RESULT_CODE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (FILE_RESULT_CODE == requestCode) {
			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {
				paths = (List<String>) bundle.getSerializable("paths");
				StringBuffer sbf = new StringBuffer();
				for(String path : paths){
					LogUtils.e(path);
					sbf.append(path + "\n");
				}
				filetext.setText(sbf.toString());
			}
		}
	}

}
