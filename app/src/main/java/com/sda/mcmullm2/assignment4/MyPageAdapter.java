package com.sda.mcmullm2.assignment4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {

  public static final int TAB1 = 0;
  public static final int TAB2 = 1;
  public static final int TAB3 = 2;
  public static final int TAB4 = 3;
  int mNumOfTabs;
  String[] tabTitles;

  public MyPageAdapter(FragmentManager fm, String[] titles) {
    super(fm);
    this.mNumOfTabs = titles.length;
    tabTitles = titles;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case TAB1:
        return new FragmentHome();
      case TAB2:
        return new FragmentProducts();
      case TAB3:
        return new FragmentOrders();
      case TAB4:
        return new Tab4Fragment();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }

  public CharSequence getPageTitle(int position) {
    if (tabTitles != null && position <= tabTitles.length) {
      return tabTitles[position];
    } else {
      return null;
    }
  }
}