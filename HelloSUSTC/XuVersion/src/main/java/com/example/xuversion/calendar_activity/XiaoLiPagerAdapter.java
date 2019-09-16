package com.example.xuversion.calendar_activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xuversion.model.XiaoLi;

import java.util.ArrayList;

public class XiaoLiPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<XiaoLi> xiaoLiList;

    /**
     * constructor
     * @param fm FragmentManager
     * @param xiaoLiList xiaoli list
     */
    public XiaoLiPagerAdapter(FragmentManager fm, ArrayList<XiaoLi> xiaoLiList) {
        super(fm);
        this.xiaoLiList = xiaoLiList;
    }

    /**
     * size getter
     * @return get the size of xiaoli list
     */
    public int getCount() {
        return xiaoLiList.size();
    }

    /**
     * get item at specific position
     * @param position the position
     * @return the fragment
     */
    public Fragment getItem(int position) {
        return XiaoLiFragment.newInstance(position, xiaoLiList.get(position).getImageID(),xiaoLiList.get(position).getInfo());
    }

}
