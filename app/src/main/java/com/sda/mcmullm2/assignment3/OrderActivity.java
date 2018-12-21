/*
 * Code adapted from code written by Colette Kirwan. DCU Open Education
 *
 * =======================================================================================
 *
 * Changes to the original code by Michael McMullin:
 * - New JavaDoc comments and standard comments.
 * - Removed unused variables.
 * - Updated camera functionality to comply with new API requirements.
 * - Included a new utility method, createImageFile, to simplify creating image files.
 * - Removed dialog notification on photo capture (too intrusive - Toast is sufficient)
 * - Added validation logic on form submission.
 * - Refactored code to meet Google Java Style Guide:
 *   - https://google.github.io/styleguide/javaguide.html
 *   - Replaces Hungarian notation.
 *   - Uses two-space indents.
 */

package com.sda.mcmullm2.assignment3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
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

/**
 * {@link OrderActivity} captures order information from the user, validates and launches external
 * apps for camera and email.
 */
public class OrderActivity extends AppCompatActivity {

  private static final int REQUEST_TAKE_PHOTO = 2;

  /**
   * An identifying tag used for filtering log messages, if required.
   */
  private static final String TAG = "Assign3";

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
   * A reference to the {@link TextView} used for displaying a caption on the photo in the layout
   * XML file.
   */
  private TextView imgCaption;

  /**
   * Called when {@link OrderActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   * <p>
   * This method initialises class-level references to various View objects that need to be accessed
   * in other methods. It also populates the {@link Spinner} AdapterView with values from a String
   * array stored in strings.xml.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);

    // Get a reference to the address EditText view. The original version also set some IME options,
    // but this prevented the 'newline' button from appearing on the keyboard, which I felt
    // defeated the purpose of having a multiline text box, at least in the context of an address.
    // This version requires users to dismiss the keyboard by clicking on another field, or using
    // the phone's back button. Online opinions suggest this is standard, familiar practice,
    // although I suspect it might be troublesome for some users. Having both 'Return' and 'Done'
    // buttons would be better I think, but I couldn't figure out how to do that yet.
    editDelivery = findViewById(R.id.editOptional);

    // Get a reference to the remaining Views in the layout file.
    spinner = findViewById(R.id.spinner);
    customerName = findViewById(R.id.editCustomer);
    imgThumbnail = findViewById(R.id.imageView);
    imgCaption = findViewById(R.id.imageText);
    editDelivery = findViewById(R.id.editOptional);

    // Create an ArrayAdapter using the resource string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.ui_time_entries, R.layout.spinner_days);
    spinner.setAdapter(adapter);
  }

  /**
   * Launches the Camera app, and prepares for returning the photo.
   * <p>
   * As this app is targeting API 28, there are now restrictions on using local files. The new
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
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // Initialise the File object to store the photo returned by the camera app.
    File file = null;
    try {
      // Get a reference to the File used to store the returned photo.
      file = createImageFile();
    } catch (IOException ex) {
      // If a File can't be created, log the exception and return.
      Log.e(TAG, ex.getMessage());
      return;
    }

    photoPath = file.getAbsolutePath();

    photoUri = FileProvider.getUriForFile(OrderActivity.this,
        BuildConfig.APPLICATION_ID + ".provider",
        file);

    // DEBUG: make sure photoUri is being set correctly.
    Log.i(TAG, photoUri.toString());

    // Store the photo URI as extra data in the Intent.
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
    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    // Try and return the File we need for temporarily storing the camera's image data.
    return File.createTempFile(imageFileName, ".jpg", dir);
  }

  /**
   * Handles the returned data from the Camera app.
   * <p>
   * Reference: <a href="https://developer.android.com/training/camera/photobasics#java">Source</a>
   *
   * @param requestCode The request code sent to the Activity through the
   *     startActivityForResult() method.
   * @param resultCode The result code returned by the Activity.
   * @param data The data returned from the Activity.
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Make sure we're handling the correct request, and that it returned OK from the camera app.
    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
      // Show the thumbnail on ImageView
      Uri imageUri = Uri.parse(photoPath);
      File file = new File(imageUri.getPath());
      try {
        InputStream ims = new FileInputStream(file);
        imgThumbnail.setImageBitmap(BitmapFactory.decodeStream(ims));
      } catch (FileNotFoundException ex) {
        // If the photo File can't be found, log the exception and return.
        Log.e(TAG, ex.getMessage());
        return;
      }

      // Let user know that image saved correctly using a Toast message. I removed the dialog as it
      // seemed too intrusive.
      CharSequence text = getString(R.string.photo_success);
      imgCaption.setText(R.string.photo_new_message);
      int duration = Toast.LENGTH_SHORT;
      Log.i(TAG, photoUri.toString());

      Toast toast = Toast.makeText(this, text, duration);
      toast.show();
    } else {
      Toast t = Toast.makeText(this, "Error taking photo", Toast.LENGTH_SHORT);
      t.show();
    }
  }

  /**
   * Creates a summary of the order details, ready to use in the email message body.
   * <p>
   * Email body message is created using user-input form data.
   *
   * @return Email Body Message
   */
  private String createOrderSummary() {
    // Original code replaced with StringBuilder
    StringBuilder message = new StringBuilder();

    // Add customer's name
    message.append(getString(R.string.customer_name));
    message.append(": ");
    message.append(customerName.getText().toString());
    message.append("\n\n");

    // Introduction paragraph
    message.append(getString(R.string.order_message_1));
    message.append("\n");

    // Delivery instructions
    String delivery = fixAddress(editDelivery.getText().toString());
    int deliveryTime = Integer.parseInt(((CharSequence) spinner.getSelectedItem()).toString().trim());

    if (delivery.matches("")) {
      // For collection
      message.append(getString(R.string.order_message_collect));
      message.append(" ");
      message.append(deliveryTime);
      message.append(" ");
      message.append(getString(R.string.order_message_days));
    } else {
      // For delivery
      message.append(getString(R.string.order_message_deliver));
      message.append("\n");
      message.append(delivery);
      message.append("\n\n");
      message.append(getString(R.string.order_message_deliver_time));
      message.append(" ");
      message.append(deliveryTime);
      message.append(" ");
      message.append(getString(R.string.order_message_days));
    }

    // Sign off message
    message.append("\n\n");
    message.append(getString(R.string.order_message_end));
    message.append("\n");
    message.append(customerName.getText().toString());

    return message.toString();
  }

  /**
   * Cleans up a user's address input.
   * @param address The address entered by the user through the order form.
   * @return A formatted version of the user's address.
   */
  private String fixAddress(String address) {
    String output = address.trim();

    if (!output.matches("")) {
      String[] lines = output.split("\n");
      output = "";

      for (String s : lines) {
        String line = s.trim();
        // Check if the line is blank, or only contains a comma / full-stop.
        if (!line.matches("^$|^,$|^\\.$")) {
          output += line + "\n";
        }
      }
    }
    return output.trim();
  }
  /**
   * Validates order form and launch populated email app.
   *
   * @param v The View that was clicked to trigger this method (the 'Send Order' button in this
   *     case)
   */
  public void sendEmail(View v) {
    Log.i(TAG, "Starting sendEmail method");

    // Update the customer name to remove surrounding spacing.
    String customerName = this.customerName.getText().toString().trim();
    this.customerName.setText(customerName);

    // The original version only checked the customer name wasn't blank. I've changed this to also
    // check that a photo has been taken. As the logic is a little more involved, it's been moved to
    // its own method.
    if (!isValidForm()) {
      Log.i(TAG, "Starting isValidForm block");
      // Decided the original Toast message wasn't prominent enough, using dialog instead.
      // Display dialog with error message. Uses the getValidationError() method to construct a
      // custom error message.
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle(R.string.validation_dialog_title).setMessage(getValidationError())
          .setPositiveButton(R.string.validation_confirmation_button, null).show();
    } else {
      // At this point, the form contains valid data, so prepare an Intent object to send the form
      // data to an email app's appropriate Activity.
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType("*/*");
      intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.to_email)});
      intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
      intent.putExtra(Intent.EXTRA_STREAM, photoUri); // URI of photo to send as attachment.
      intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
      if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
      }
    }
  }

  /**
   * Determines whether or not the form contains valid data.
   *
   * @return {@code true} if the form contains valid input. Otherwise, {@code false}.
   */
  protected boolean isValidForm() {
    if (this.customerName.getText().toString().matches("")) {
      return false;
    }
    if (this.imgCaption.getText().toString().matches(getString(R.string.photo_instruction))) {
      return false;
    }

    return true;
  }

  /**
   * Constructs an error message to instruct users what to change.
   *
   * @return An error message based on incorrect user inputs.
   */
  protected String getValidationError() {
    StringBuilder error = new StringBuilder();

    // Handle empty customer name.
    if (this.customerName.getText().toString().matches("")) {
      error.append(getString(R.string.error_customer_name_blank));
      error.append("\n");
    }

    // Handle empty image data.
    // TODO: Currently just checking caption. Change to more robust file checking.
    if (this.imgCaption.getText().toString().matches(getString(R.string.photo_instruction))) {
      error.append(getString(R.string.error_photo_blank));
      error.append("\n");
    }

    return error.toString();
  }
}
