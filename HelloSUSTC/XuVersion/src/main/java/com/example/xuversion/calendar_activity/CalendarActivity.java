package com.example.xuversion.calendar_activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuversion.R;
import com.example.xuversion.model.XiaoLi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{


    private int[] imageID = {R.drawable.xiaoli1, R.drawable.xiaoli2, R.drawable.xiaoli3};
    private String[] titleList = {"2018-2019 Fall Semester","2018-2019 Spring-Summer Semester",
            "2019-2020 Fall-Winter Semester"};
    private ArrayList<XiaoLi> xiaoLiList;
    @BindView(R.id.layout_return)
    ImageView returnbtn;
    @BindView(R.id.layout_name)
    TextView titleName;
    @BindView(R.id.calendar_Flipper)
    ViewPager vp_content;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        titleName.setText("School Calender");
        returnbtn.setOnClickListener(this);
        initalFlipper();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_return:
                this.finish();
                break;

            default:
                break;
        }
    }

    private void initalFlipper(){
        xiaoLiList = new ArrayList<XiaoLi>();
        for(int i = 0;i<imageID.length;i++){
            xiaoLiList.add(new XiaoLi(titleList[i],imageID[i]));
        }
        XiaoLiPagerAdapter adapter = new XiaoLiPagerAdapter(getSupportFragmentManager(),xiaoLiList);
        vp_content.setAdapter(adapter);
        vp_content.setCurrentItem(0);

    }

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("type","xiaoli");
//                bundle.putInt("nowpicture",pic);
//                Intent it = new Intent(CalendarActivity.this, PhotoViewActivity.class);
//                it.putExtras(bundle);
//                startActivity(it);
//            }
//        });
}
