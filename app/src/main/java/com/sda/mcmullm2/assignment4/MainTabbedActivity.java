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

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.addTab(tabLayout.newTab());
    tabLayout.addTab(tabLayout.newTab());
    tabLayout.addTab(tabLayout.newTab());
    tabLayout.addTab(tabLayout.newTab());

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    final ViewPager mypager = (ViewPager) findViewById(R.id.pager);
    final MyPageAdapter myadapter = new MyPageAdapter(getSupportFragmentManager(),
        tabLayout.getTabCount());

    mypager.setAdapter(myadapter);

    tabLayout.setupWithViewPager(mypager);
  }
}
