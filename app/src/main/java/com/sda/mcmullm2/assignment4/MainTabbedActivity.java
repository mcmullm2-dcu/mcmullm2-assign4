package com.sda.mcmullm2.assignment4;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

public class MainTabbedActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tabbed);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Set up tabs
    TabLayout tabLayout = findViewById(R.id.tab_layout);
    String[] tabTitles = getResources().getStringArray(R.array.tab_titles);
    for(int i=0; i<tabTitles.length; i++) {
      tabLayout.addTab(tabLayout.newTab());
    }

    // Create the adapter that will return a fragment for each of the
    // primary sections of the activity.
    final ViewPager mypager = findViewById(R.id.pager);
    final MyPageAdapter myadapter = new MyPageAdapter(getSupportFragmentManager(), tabTitles);

    mypager.setAdapter(myadapter);

    tabLayout.setupWithViewPager(mypager);
  }
}
