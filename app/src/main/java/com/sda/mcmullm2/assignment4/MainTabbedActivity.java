package com.sda.mcmullm2.assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

public class MainTabbedActivity extends AppCompatActivity {
  private final String TAG = "Assign4";

  private final int HOME_TAB_INDEX = 0;
  private final int PRODUCTS_TAB_INDEX = 1;
  private final int ORDER_TAB_INDEX = 2;
  private final int COLLECTION_TAB_INDEX = 3;

  String[] tabTitles;
  TabLayout tabLayout;

  SharedPreferences prefs;
  private FragmentProducts productsFragment;
  private FragmentOrders ordersFragment;
  private FragmentCollection collectionFragment;

  private String prefs_customerName = "";
  private Set<String> prefs_products = null;
  // private int prefs_deliveryTime = DEFAULT_DELIVERY_TIME;
  private String prefs_address = "";
  private Boolean prefs_isCollection = false;
  private String prefs_imagePath = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tabbed);

    prefs = getPreferences(MODE_PRIVATE);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Set up tabs
    tabLayout = findViewById(R.id.tab_layout);
    tabTitles = getResources().getStringArray(R.array.tab_titles);
    for(int i=0; i<tabTitles.length; i++) {
      tabLayout.addTab(tabLayout.newTab());
    }
    // Ensure 'Home' tab is presented to the user
    setTab(HOME_TAB_INDEX);

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
          // Open email app
          // At this point, the form contains valid data, so prepare an Intent object to send the form
          // data to an email app's appropriate Activity.
          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setType("*/*");
          intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.to_email)});
          intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));

          // Attach photo (if present) to email
          Uri photo = getPhoto(prefs_imagePath);
          if (photo != null) {
            Log.i(TAG, "Photo URI");
            Log.i(TAG, photo.toString());
            intent.putExtra(Intent.EXTRA_STREAM, photo);
          }

          // Add email body text
          intent.putExtra(Intent.EXTRA_TEXT, summary.toString());
          if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
          }
          return true;
        }
    }
    return false;
  }

  /**
   * Given a file path, get a URI to the photo contained in it.
   * @param filepath
   * @return
   */
  private Uri getPhoto(String filepath) {
    if (filepath.matches("")) return null;

    File imageFile = new File(filepath);
    Uri photoUri = FileProvider.getUriForFile(this,
          BuildConfig.APPLICATION_ID + ".provider",
          imageFile);
    return photoUri;
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

  /**
   * Validate and set any remaining SharedPreference data.
   * @return <c>true</c> if the existing SharedPreferences are valid, otherwise <c>false</c>
   */
  private boolean setPreferences() {
    Log.i(TAG, "Setting preferences");

    if (prefs == null) {
      Toast.makeText(this, getString(R.string.error_no_prefs), Toast.LENGTH_SHORT).show();
      return false;
    }
    if (isValidOrder()) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString("Address", prefs_address);
      editor.commit();
      return true;
    }
    return false;
  }

  /**
   * Checks if the order is valid.
   *
   * @return
   */
  private boolean isValidOrder() {
    if (prefs == null) {
      Toast.makeText(this, getString(R.string.error_no_prefs), Toast.LENGTH_SHORT).show();
      return false;
    }

    getPreferences();

    // Check products selected
    if (prefs_products == null || prefs_products.size() == 0) {
      setTab(PRODUCTS_TAB_INDEX);
      Toast.makeText(this, getString(R.string.error_no_products), Toast.LENGTH_SHORT).show();
      return false;
    }

    // Check customer name
    if (prefs_customerName.isEmpty()) {
      setTab(ORDER_TAB_INDEX);
      Toast.makeText(this, getString(R.string.error_customer_name_blank), Toast.LENGTH_SHORT).show();
      return false;
    }

    // Check address
    if (prefs_address.isEmpty()) {
      setTab(COLLECTION_TAB_INDEX);
      Toast.makeText(this, getString(R.string.error_no_address), Toast.LENGTH_LONG).show();
      return false;
    }

    return true;
  }

  private void getPreferences() {
    if (prefs != null) {
      prefs_isCollection = prefs.getBoolean("IsCollection", false);
      if (prefs_isCollection) {
        prefs_address = prefs.getString("CollectionAddress", "");
      } else {
        prefs_address = prefs.getString("DeliveryAddress", "");
      }
    }
    prefs_customerName = prefs.getString("CustomerName", "");
    prefs_products = prefs.getStringSet("Products", null);
    prefs_imagePath = prefs.getString("ImagePath", "");
  }

  private void setTab(int index) {
    if (tabLayout != null && tabLayout.getTabCount() > index) {
      tabLayout.getTabAt(index).select();
    }
  }
}
