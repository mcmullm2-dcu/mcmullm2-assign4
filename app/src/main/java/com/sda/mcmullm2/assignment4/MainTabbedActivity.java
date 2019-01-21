package com.sda.mcmullm2.assignment4;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainTabbedActivity extends AppCompatActivity {
  private final String TAG = "Assign4";

  private final int HOME_TAB_INDEX = 0;
  String[] tabTitles;
  SharedPreferences prefs;
  private FragmentProducts productsFragment;
  private FragmentOrders ordersFragment;
  private FragmentCollection collectionFragment;

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
    Log.i(TAG, "Starting onOptionsItemSelected method");

    switch (item.getItemId()) {
      case R.id.menuitem_send:
        Log.i(TAG, "menuitem_send");
        if (setPreferences()) {
          EmailSummary summary = new EmailSummary(this.getApplicationContext(), prefs);
          // TODO: open email app
          Toast.makeText(this, summary.toString(), // getString(R.string.button_place_order),
              Toast.LENGTH_SHORT).show();
          return true;
        }
    }
    return false;
  }

  /**
   * https://developer.android.com/training/basics/fragments/communicating
   *
   * @param fragment
   */
  @Override
  public void onAttachFragment(Fragment fragment) {
    super.onAttachFragment(fragment);

    if (productsFragment == null && fragment instanceof FragmentProducts) {
      Log.i(TAG, "Products fragment added to main activity");
      productsFragment = (FragmentProducts) fragment;
    } else if (ordersFragment == null && fragment instanceof FragmentOrders) {
      Log.i(TAG, "Orders fragment added to main activity");
      ordersFragment = (FragmentOrders) fragment;
    } else if (collectionFragment == null && fragment instanceof FragmentCollection) {
      Log.i(TAG, "Collection fragment added to main activity");
      collectionFragment = (FragmentCollection) fragment;
    }
  }

  private boolean setPreferences() {
    Log.i(TAG, "Setting preferences");

    if (prefs == null) {
      Toast.makeText(this, getString(R.string.error_no_prefs), Toast.LENGTH_SHORT).show();
      return false;
    }
    if (productsFragment == null) return false;
    if (ordersFragment == null) return false;

    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("CustomerName", ordersFragment.getTest());
    editor.commit();
    return true;
  }
}
