package com.sda.mcmullm2.assignment4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {

  public static final int TAB1 = 0;
  public static final int TAB2 = 1;
  public static final int TAB3 = 2;
  public static final int TAB4 = 3;
  public static final String UI_TAB_1 = R.string.tab_1_title;
  public static final String UI_TAB_2 = R.string.tab_2_title;
  public static final String UI_TAB_3 = R.string.tab_3_title;
  public static final String UI_TAB_4 = R.string.tab_4_title;
  int mNumOfTabs;

  public MyPageAdapter(FragmentManager fm, int NumOfTabs) {
    super(fm);
    this.mNumOfTabs = NumOfTabs;
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
    switch (position) {
      case TAB1:
        return UI_TAB_1;
      case TAB2:
        return UI_TAB_2;
      case TAB3:
        return UI_TAB_3;
      case TAB4:
        return UI_TAB_4;
      default:
        break;
    }
    return null;
  }


}