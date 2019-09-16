package com.example.xuversion.calendar_activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xuversion.R;

public class XiaoLiFragment extends Fragment {
    protected View mView;
    private int mImageId;
    private String mtitle;

    /**
     * Constructor
     * @param position the position for the view
     * @param image_id the image for the view
     * @param title the title for the view
     * @return fragment
     */
    public static XiaoLiFragment newInstance(int position, int image_id, String title) {
        XiaoLiFragment fragment = new XiaoLiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putInt("image_id", image_id);
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * on create event
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState last state
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mImageId = getArguments().getInt("image_id", 0);
            mtitle = getArguments().getString("title");
        }
        //生成视图对象
        mView = inflater.inflate(R.layout.item_calendar, container, false);
        ImageView imageView = (ImageView)mView.findViewById(R.id.xiaoli);
        TextView textView = (TextView)mView.findViewById(R.id.xiaolitop);
        textView.setText(mtitle);
        imageView.setImageResource(mImageId);
        return mView;
    }

}
