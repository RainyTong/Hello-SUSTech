package com.example.xuversion.class_table_activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

public class ClassViewAdapter extends PagerAdapter{
//    private Context mContext;
    private ArrayList<ClassView> mViewList;
    private ArrayList<String> mTitleList = new ArrayList<String>();

    /**
     * For the whole class view
     * @param context Context
     * @param viewList List of views
     */
    public ClassViewAdapter(Context context,ArrayList<ClassView> viewList) {
//        this.mContext = context;
        this.mViewList = viewList;
        initialTitleList();
    }

    /**
     * Initialize all days
     */
    private void initialTitleList(){
        mTitleList.add("周一");
        mTitleList.add("周二");
        mTitleList.add("周三");
        mTitleList.add("周四");
        mTitleList.add("周五");
        mTitleList.add("周六");
        mTitleList.add("周日");

    }

    /**
     * Get this size of current list of the class
     * @return the size
     */
    @Override
    public int getCount() {
        return mViewList.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object as returned by instantiateItem
     * @param arg0 the page view
     * @param arg1 the key object
     * @return if associated
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    /**
     * delete the item at specified position
     * @param container the container of the views
     * @param position the index for the view
     * @param object not used
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    /**
     * Instantiate a view into the ViewGroup from current ViewList
     * @param container the container for all the views
     * @param position the position for the view that want instantiate
     * @return the instantiated view
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    /**
     * get the title for specific page
     * @param position the postion for the page
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
    
//    public ArrayList<ClassView> getmViewList() {
//        return mViewList;
//    }
}
