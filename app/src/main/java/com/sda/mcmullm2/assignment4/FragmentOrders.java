package com.sda.mcmullm2.assignment4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentOrders extends Fragment implements OnClickListener {

  private static SharedPreferences prefs;
  private final int DEFAULT_DELIVERY_TIME = 3;

  /**
   * Default empty constructor
   */
  public FragmentOrders() {
  }

  /**
   * Factory method to create a new instance of FragmentOrders
   * @param preferences
   * @return
   */
  public static FragmentOrders newInstance(SharedPreferences preferences) {
    FragmentOrders fragment = new FragmentOrders();
    prefs = preferences;
    return fragment;
  }

  /**
   * Get the name entered by the customer.
   *
   * @return
   */
  public String getCustomerName() {
    if (customerName != null) {
      return customerName.getText().toString().trim();
    }
    return "";
  }

  public void setCustomerName(String name) {
    if (customerName != null) {
      customerName.setText(name);
    }
  }

  /**
   * Get the delivery address entered by the customer, which is optional if they pick a collection
   * point instead.
   *
   * @return
   */
  public String getCustomerAddress() {
    if (editDelivery != null) {
      return fixAddress(editDelivery.getText().toString());
    }
    return "";
  }

  public void setCustomerAddress(String address) {
    if (editDelivery != null) {
      editDelivery.setText(address);
    }
  }

  /**
   * Get the delivery/collection time (in days) requested by the customer.
   *
   * @return
   */
  public int getDeliveryTime() {
    if (spinner != null) {
      return Integer.parseInt(((CharSequence) spinner.getSelectedItem()).toString().trim());
    }
    return 0;
  }

  public void setDeliveryTime(int days) {
    String dayStr = Integer.toString(days);
    if (spinner != null) {
      for (int i=0; i<spinner.getCount(); i++) {
        String item = spinner.getItemAtPosition(i).toString().trim();
        if (item.equals(dayStr)) {
          spinner.setSelection(i);
          break;
        }
      }
    }
  }

  /**
   * Get the URI of the photo taken, if any.
   *
   * @return
   */
  public Uri getPhotoUri() {
    return photoUri;
  }

  private static final int REQUEST_TAKE_PHOTO = 2;

  /**
   * An identifying tag used for filtering log messages, if required.
   */
  private static final String TAG = "Assign4";
  private Uri photoUri;
  private String photoPath;
  /**
   * A reference to the {@link Spinner} AdapterView used for selecting days in the layout XML file.
   */
  private Spinner spinner;

  /**
   * A reference to the {@link EditText} View used for entering customer's name in the layout XML
   * file.
   */
  private EditText customerName;

  /**
   * A reference to the {@link EditText} View used for entering customer's address in the layout XML
   * file.
   */
  private EditText editDelivery;

  /**
   * A reference to the {@link ImageView} used for displaying the user's photo (or default image) in
   * the layout XML file.
   */
  private ImageView imgThumbnail;

  /**
   * A reference to the {@Link File} object used for saving the user's photo.
   */
  private File imageFile;

  /**
   * A reference to the {@link TextView} used for displaying a caption on the photo in the layout
   * XML file.
   */
  private TextView imgCaption;

  /**
   * Flag indicating that a photo has been taken. Useful for situations where a user tries, then
   * cancels, taking a replacement photo.
   */
  private boolean photoTaken = false;

  /**
   * This method initialises class-level references to various View objects that need to be
   * accessed in other methods. It also populates the {@link Spinner} AdapterView with values from a
   * String array stored in strings.xml.
   *
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      final Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_orders, container, false);

    // Get a reference to the address EditText view. The original version also set some IME options,
    // but this prevented the 'newline' button from appearing on the keyboard, which I felt
    // defeated the purpose of having a multiline text box, at least in the context of an address.
    // This version requires users to dismiss the keyboard by clicking on another field, or using
    // the phone's back button. Online opinions suggest this is standard, familiar practice,
    // although I suspect it might be troublesome for some users. Having both 'Return' and 'Done'
    // buttons would be better I think, but I couldn't figure out how to do that yet.
    editDelivery = view.findViewById(R.id.editOptional);

    // Get a reference to the remaining Views in the layout file.
    spinner = view.findViewById(R.id.spinner);
    customerName = view.findViewById(R.id.editCustomer);
    imgThumbnail = view.findViewById(R.id.imageView);
    imgCaption = view.findViewById(R.id.imageText);
    editDelivery = view.findViewById(R.id.editOptional);

    // Set click events
    imgThumbnail.setOnClickListener(this);
    imgCaption.setOnClickListener(this);

    // Create an ArrayAdapter using the resource string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
        R.array.ui_time_entries, R.layout.spinner_days);
    spinner.setAdapter(adapter);

    // Restore image data if necessary
    if (savedInstanceState != null) {
      try {
        initPhoto(savedInstanceState.getString("imagePath"));
      } catch (NullPointerException e){
        Log.w(TAG, "Cannot find imagePath");
      }
    }
    restoreFromSharedPrefs();

    // Handle saving the current user input to shared preferences when text has been updated.
    // Source: https://alvinalexander.com/source-code/android/android-programming-how-save-edittext-changes-without-save-button-ie-text-change
    TextWatcher watcher = new TextWatcher() {
      // the user's changes are saved here
      public void onTextChanged(CharSequence c, int start, int before, int count) {
        saveSharedPrefs();
      }
      public void beforeTextChanged(CharSequence c, int start, int count, int after) {
        // this space intentionally left blank
      }
      public void afterTextChanged(Editable c) {
        // this one too
      }
    };

    customerName.addTextChangedListener(watcher);
    editDelivery.addTextChangedListener(watcher);
    // Save the order data when a new spinner value is selected
    // Source: https://stackoverflow.com/a/30497485/5233918
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          saveSharedPrefs();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });

    return view;
  }

  /**
   * Click event handler for the various views on this page.
   *
   * <a href="https://stackoverflow.com/a/14571018/5233918">Click events in fragments</a>
    *
   * @param view The View that was clicked to trigger this method
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.imageView:
      case R.id.imageText:
        dispatchTakePictureIntent(view);
        break;
    }
  }

  /**
   * When this Activity needs to be recreated, this method gives an opportunity to save some data
   * that can be restored again later.
   *
   * <p>In this case, we're saving the path to a photo that the user has taken so that it can be
   * reloaded in the onCreate method. This allows us to rotate the screen without reverting back to
   * the default image.
   *
   * <p>Reference: https://developer.android.com/guide/components/activities/activity-lifecycle#java
   *
   * @param state A bundle of key/value pairs to store our app's state.
   */
  @Override
  public void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);

    if (isPhotoTaken()) {
      state.putString("imagePath", imageFile.getPath());
    }

    saveSharedPrefs();
  }

  /**
   * Updates the SharedPreferences data with the values entered by the user.
   */
  void saveSharedPrefs() {
    if (prefs != null) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString("CustomerName", getCustomerName());
      editor.putString("DeliveryAddress", getCustomerAddress());
      String fileName = isPhotoTaken() ? imageFile.getPath() : "";
      editor.putString("ImagePath", fileName);
      editor.putInt("DeliveryTime", getDeliveryTime());
      editor.commit();
    }
  }

  /**
   * Populates the text views and spinner with data saved in SharedPreferences.
   */
  void restoreFromSharedPrefs() {
    if (prefs != null) {
      setCustomerName(prefs.getString("CustomerName", ""));
      setCustomerAddress(prefs.getString("DeliveryAddress", ""));
      setDeliveryTime(prefs.getInt("DeliveryTime", DEFAULT_DELIVERY_TIME));
    }
  }

  /**
   * Re-initialise the user's photo and related data based on a supplied filepath.
   *
   * @param filepath The path to the user's photo.
   */
  private void initPhoto(String filepath) {
    if (filepath.matches("")) return;

    try {
      imageFile = new File(filepath);
      InputStream ims = new FileInputStream(imageFile);
      imgThumbnail.setImageBitmap(BitmapFactory.decodeStream(ims));
      imgCaption.setText(R.string.photo_new_message);
      photoPath = imageFile.getAbsolutePath();
      photoUri = FileProvider.getUriForFile(this.getContext(),
          BuildConfig.APPLICATION_ID + ".provider",
          imageFile);
    } catch (FileNotFoundException ex) {
      Log.e(TAG, ex.getMessage());
    }
  }

  /**
   * Launches the Camera app, and prepares for returning the photo.
   *
   * <p>As this app is targeting API 28, there are now restrictions on using local files. The new
   * techniques are described in the following links, which were referred to in order to fix the
   * original method that targeted API 23 and below:
   * <ul>
   *   <li><a href="https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en">Source
   * 1</a></li>
   *   <li><a href="https://rahulupadhyay.wordpress.com/2016/12/27/android-n-fileuriexposedexception/">Source
   * 2</a></li>
   * </ul>
   *
   * @param v The View that was clicked to trigger this method (the photo in this case)
   */
  public void dispatchTakePictureIntent(View v) {
    // Initialise the File object to store the photo returned by the camera app.
    try {
      // Get a reference to the File used to store the returned photo.
      imageFile = createImageFile();
    } catch (IOException ex) {
      // If a File can't be created, log the exception and return.
      Log.e(TAG, ex.getMessage());
      return;
    }

    photoPath = imageFile.getAbsolutePath();
    photoUri = FileProvider.getUriForFile(this.getContext(),
        BuildConfig.APPLICATION_ID + ".provider",
        imageFile);

    // DEBUG: make sure photoUri is being set correctly.
    Log.i(TAG, photoUri.toString());

    // Store the photo URI as extra data in the Intent.
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

    // Launch the external camera app's activity and indicate we want to receive its result.
    startActivityForResult(intent, REQUEST_TAKE_PHOTO);

    // In case of caching if it comes from the activity stack, just a precaution
    intent.removeExtra(MediaStore.EXTRA_OUTPUT);
  }

  /**
   * Create a File object that can used to store the image returned from the camera app.
   *
   * @return A time-stamped temporary File object ready to store camera app image.
   * @throws IOException The File could not be created.
   */
  private File createImageFile() throws IOException {
    // Construct a unique filename based on current date/time.
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "my_t_shirt_image_" + timeStamp;

    // Get the path to the directory where the app can place its own images.
    File dir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    // Try and return the File we need for temporarily storing the camera's image data.
    return File.createTempFile(imageFileName, ".jpg", dir);
  }

  /**
   * Handles the returned data from the Camera app.
   *
   * <p>Reference: <a href="https://developer.android.com/training/camera/photobasics#java">Source</a>
   *
   * @param requestCode The request code sent to the Activity through the
   *     startActivityForResult() method.
   * @param resultCode The result code returned by the Activity.
   * @param data The data returned from the Activity.
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Make sure we're handling the correct request, and that it returned OK from the camera app.
    if (requestCode == REQUEST_TAKE_PHOTO) {
      if (resultCode == Activity.RESULT_OK) {
        // Show the thumbnail on ImageView
        initPhoto(photoPath);

        // Let user know that image saved correctly using a Toast message. I removed the dialog as it
        // seemed too intrusive.
        CharSequence text = getString(R.string.photo_success);
        int duration = Toast.LENGTH_SHORT;
        Log.i(TAG, photoUri.toString());
        photoTaken = true;

        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();
      } else {
        if (!photoTaken) {
          imageFile = null;
          photoUri = null;
        }
        Toast t = Toast.makeText(getActivity(), "Error taking photo", Toast.LENGTH_SHORT);
        t.show();
      }
    }
  }

  /**
   * Cleans up a user's address input.
   * @param address The address entered by the user through the order form.
   * @return A formatted version of the user's address.
   */
  private String fixAddress(String address) {
    String output = address.trim();
    StringBuilder sb = new StringBuilder();

    if (!output.matches("")) {
      String[] lines = output.split("\n");

      for (String s : lines) {
        String line = s.trim();
        // Check if the line is blank, or only contains a comma / full-stop.
        if (!line.matches("^$|^,$|^\\.$")) {
          sb.append(line).append("\n");
        }
      }
    }
    output = sb.toString();

    return output.trim();
  }

  /**
   * Determines whether or not the user has taken a photo.
   *
   * @return {@code true} if the user has taken a photo. Otherwise, {@code false}
   */
  private boolean isPhotoTaken() {
    return imageFile != null;
  }

}
