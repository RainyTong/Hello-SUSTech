package com.example.xuversion.welcome_activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

public class LaunchImproveAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Integer> mImageList = new ArrayList<Integer>();

    /**
     * Constructor
     * @param fm FragmentManager object
     * @param imageArray list to store the images
     */
	public LaunchImproveAdapter(FragmentManager fm, int[] imageArray) {
		super(fm);
		for (int i=0; i<imageArray.length; i++) {
			mImageList.add(imageArray[i]);
		}
	}

	/**
	 * Get the size of mImageList
	 * @return the size of mImageList
	 */
	public int getCount() {
		return mImageList.size();
	}

    /**
     * Get item at specific postion
     * @param position the position
     * @return the item
     */
	public Fragment getItem(int position) {
		return LaunchFragment.newInstance(position, mImageList.get(position));
	}

}
