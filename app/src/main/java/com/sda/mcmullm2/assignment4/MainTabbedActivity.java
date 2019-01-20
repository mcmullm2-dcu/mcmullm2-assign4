package com.sda.mcmullm2.assignment4;

import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainTabbedActivity extends AppCompatActivity {
  private final int HOME_TAB_INDEX = 0;
  String[] tabTitles;
  SharedPreferences prefs;
  private String customerName;
  private String address;
  private int deliveryTime;
  private boolean isCollection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tabbed);

    prefs = getPreferences(MODE_PRIVATE);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Set up tabs
    TabLayout tabLayout = findViewById(R.id.tab_layout);
    tabTitles = getResources().getStringArray(R.array.tab_titles);
    for(int i=0; i<tabTitles.length; i++) {
      tabLayout.addTab(tabLayout.newTab());
    }
    // Ensure 'Home' tab is presented to the user
    tabLayout.getTabAt(HOME_TAB_INDEX).select();

    // Create the adapter that will return a fragment for each of the
    // primary sections of the activity.
    final ViewPager mypager = findViewById(R.id.pager);
    final MyPageAdapter myadapter = new MyPageAdapter(getSupportFragmentManager(), tabTitles, prefs);

    mypager.setAdapter(myadapter);

    tabLayout.setupWithViewPager(mypager);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menuitem_send:
        setPreferences();
        EmailSummary summary = new EmailSummary(this.getApplicationContext(), prefs);
        Toast.makeText(this, summary.toString(), // getString(R.string.button_place_order),
            Toast.LENGTH_SHORT).show();
        return true;
    }
    return false;
  }

  private void setPreferences() {
    if (tabTitles == null && tabTitles.length < 4) return;

    // https://learnpainless.com/android/how-to-get-fragment-from-viewpager-android
    FragmentOrders ordersFragment = (FragmentOrders) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);

    if (prefs != null) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString("CustomerName", ordersFragment.getTest());
      editor.commit();
    }
  }
}
