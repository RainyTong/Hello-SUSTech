package com.example.xuversion.welcome_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.xuversion.R;
import com.example.xuversion.main_activity.MainActivity;


public class LaunchFragment extends Fragment {
	protected View mView;
	protected Context mContext;
	private int mPosition;
	private int mImageId;
	private int mCount = 4;

	/**
	 * Constructor
	 * @param position the position
	 * @param image_id the image id
	 * @return the fragment
	 */
	public static LaunchFragment newInstance(int position, int image_id) {
		LaunchFragment fragment = new LaunchFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putInt("image_id", image_id);
		fragment.setArguments(bundle);
		return fragment;
	}

	/**
	 * The lifecycle method
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return the view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		if (getArguments() != null) {
			mPosition = getArguments().getInt("position", 0);
			mImageId = getArguments().getInt("image_id", 0);
		}
		mView = inflater.inflate(R.layout.item_launch, container, false);
		ImageView iv_launch = (ImageView) mView.findViewById(R.id.iv_launch);
		RadioGroup rg_indicate = (RadioGroup) mView.findViewById(R.id.rg_indicate);
		Button btn_start = (Button) mView.findViewById(R.id.btn_start);
		
		iv_launch.setImageResource(mImageId);
		//init RadioButton and btn_start
		for (int j=0; j<mCount; j++) {
			RadioButton radio = new RadioButton(mContext);
			RadioGroup.LayoutParams dotParams = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			radio.setLayoutParams(dotParams);

			radio.setButtonDrawable(R.drawable.ui_launch);
			radio.setPadding(10, 10, 10, 10);
			rg_indicate.addView(radio);
		}
		((RadioButton)rg_indicate.getChildAt(mPosition)).setChecked(true);
		if (mPosition == mCount-1) {
			btn_start.setVisibility(View.VISIBLE);
			btn_start.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent(getActivity(),MainActivity.class);
					startActivity(it);
					getActivity().finish();//关闭当前
				}
			});
		}
		return mView;
	}

}
