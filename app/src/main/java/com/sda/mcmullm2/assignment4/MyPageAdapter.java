package com.sda.mcmullm2.assignment4;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {

  public static final int TAB_HOME = 0;
  public static final int TAB_PRODUCTS = 1;
  public static final int TAB_ORDER = 2;
  public static final int TAB_COLLECTION = 3;
  int mNumOfTabs;
  String[] tabTitles;
  SharedPreferences prefs;

  public MyPageAdapter(FragmentManager fm, String[] titles, SharedPreferences prefs) {
    super(fm);
    this.mNumOfTabs = titles.length;
    tabTitles = titles;
    this.prefs = prefs;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case TAB_HOME:
        return new FragmentHome();
      case TAB_PRODUCTS:
        return FragmentProducts.newInstance(prefs);
      case TAB_ORDER:
        return FragmentOrders.newInstance(prefs);
      case TAB_COLLECTION:
        return FragmentCollection.newInstance(prefs);
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