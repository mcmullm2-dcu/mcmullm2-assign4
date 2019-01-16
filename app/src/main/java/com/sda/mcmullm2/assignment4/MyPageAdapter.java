package com.sda.mcmullm2.assignment4;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {

  public static final int TAB1 = 0;
  public static final int TAB2 = 1;
  public static final int TAB3 = 2;
  public static final int TAB4 = 3;
  public static final String UI_TAB_1 = "Home";
  public static final String UI_TAB_2 = "Products";
  public static final String UI_TAB_3 = "Orders";
  public static final String UI_TAB_4 = "Collection";
  int mNumOfTabs;
  String[] tabTitles;

  public MyPageAdapter(FragmentManager fm, int NumOfTabs, String[] titles) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
    tabTitles = titles;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case TAB1:
        return new Tab1Fragment();
      case TAB2:
        return new Tab2Fragment();
      case TAB3:
        return new Tab3Fragment();
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