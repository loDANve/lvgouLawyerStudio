package com.lvgou.lawyer.studio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lvgou.lawyer.studio.R;
import com.lvgou.lawyer.studio.activity.AccountDredgeActivity;
import com.lvgou.lawyer.studio.activity.BankManagerActivity;
import com.lvgou.lawyer.studio.activity.SecurityActivity;
import com.lvgou.lawyer.studio.activity.UserInfoActivity;
import com.lvgou.lawyer.studio.been.MyCenter;
import com.lvgou.lawyer.studio.utils.Utils;

/**
 * 个人中心Fragment
 * 
 * @author xiangyu
 * 
 * @notes Created on 2014-4-17
 */
public class MyCenterFragment extends BaseFragment implements OnClickListener {
	private static MyCenterFragment instance;

	/**
	 * 单例模式
	 * 
	 * @return fragment
	 */
	public static MyCenterFragment getInstance() {
		if (instance == null) {
			instance = new MyCenterFragment();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	public MyCenterFragment() {
		super();
	}

	@ViewInject(R.id.userinfo_rl)
	private RelativeLayout userinfo_rl;
	@ViewInject(R.id.account_rl)
	private RelativeLayout account_rl;
	@ViewInject(R.id.bankcard_rl)
	private RelativeLayout bankcard_rl;
	@ViewInject(R.id.safecenter_btn)
	private ImageButton safecenter_btn;
	@ViewInject(R.id.username)
	private TextView username;
	@ViewInject(R.id.phonenumber)
	private TextView phonenumber;
	@ViewInject(R.id.balance)
	private TextView balance;
	@ViewInject(R.id.cardcount)
	private TextView cardcount;
	@ViewInject(R.id.notificationcount)
	private TextView notificationcount;
	@ViewInject(R.id.hotline)
	private TextView hotline;
	@ViewInject(R.id.headimg)
	private ImageView headimg;

	private HttpUtils http;
	private MyCenter mc;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		instance = this;
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_mycenter_fragment, container,
				false);
		ViewUtils.inject(this, v);
		userinfo_rl.setOnClickListener(this);
		account_rl.setOnClickListener(this);
		safecenter_btn.setOnClickListener(this);
		bankcard_rl.setOnClickListener(this);
		getMyCenterData();
		return v;
	}

	private void getMyCenterData() {
		mc = new MyCenter();
		mc.setLawyerid(Utils.getLawyerid(getActivity()));
		http = new HttpUtils(5000);
		// http.cl
		http.send(HttpRequest.HttpMethod.GET, mc.getUrl(),
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (null == getActivity()
								|| getActivity().isFinishing()) {
							return;
						}
						mc = Utils.json2Object(responseInfo.result.getBytes(),
								mc);
						if (null != mc && mc.getCode() == 1) {
							username.setText(mc.getRealname()
									+ mc.getLawyerlevel() + "级("
									+ mc.getLevelNotice() + ")");
							phonenumber.setText(mc.getMobile());
							cardcount.setText(mc.getBankNums() + "张");
							notificationcount.setText(mc.getUnreadNums() + "");
							hotline.setText("客服热线 :  " + mc.getServiceTel());
							BitmapUtils bu = new BitmapUtils(getActivity());
							bu.display(headimg, mc.getUserImg());
							if(mc.getOpenaccount() == 0){
								balance.setText(mc.getBalance());
							}else{
								balance.setText("未开通");
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.userinfo_rl:
			intent = new Intent(getActivity(), UserInfoActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.account_rl:
			intent = new Intent(getActivity(), AccountDredgeActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.safecenter_btn:
			intent = new Intent(getActivity(), SecurityActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.bankcard_rl:
			intent = new Intent(getActivity(), BankManagerActivity.class);
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}

	}
}
