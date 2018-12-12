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
        import android.widget.Toast;

        import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
        import java.util.Date;

//Adapteed from code written by Colette Kirwan. DCU Open Education

public class OrderActivity extends AppCompatActivity
{
    Uri mPhotoURI;
    String photoPath;
    Spinner mSpinner;
    EditText mCustomerName;
    EditText meditOptional;
    ImageView mimageThumbnail;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "Assign3";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        meditOptional = (EditText) findViewById(R.id.editOptional);

        meditOptional.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditOptional.setRawInputType(InputType.TYPE_CLASS_TEXT);
        //initialise spinner using the integer array
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mCustomerName = (EditText) findViewById(R.id.editCustomer);
        mimageThumbnail = (ImageView) findViewById(R.id.imageView);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);

    }

    /**
     *
     * <a href="https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en">Source 1</a>
     * <a href="https://rahulupadhyay.wordpress.com/2016/12/27/android-n-fileuriexposedexception/">Source 2</a>
     * @param v
     *
     */
    public void dispatchTakePictureIntent(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = null;
        try {
            file = createImageFile();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }

        photoPath = file.getAbsolutePath();

        mPhotoURI = FileProvider.getUriForFile(OrderActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        Log.i(TAG, mPhotoURI.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        //incase of caching if it comes from the activity stack, just a precaution
        intent.removeExtra(MediaStore.EXTRA_OUTPUT);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "my_tshirt_image_" + timeStamp; // + ".jpg";

        Log.i(TAG, "imagefile");

        // File file = new File(Environment.getExternalStorageDirectory(), imageFileName);
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(imageFileName, ".jpg", dir);

        return file;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //also can give user a message that everything went ok
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(photoPath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                mimageThumbnail.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            //Bundle extras = data.getExtras();
            //Bitmap img = (Bitmap) extras.get("data");
            //mimageThumbnail.setImageBitmap(img);

            //let user know that image saved
            //I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            CharSequence text = "Image Taken successfully";
            int duration = Toast.LENGTH_SHORT;
            Log.i(TAG, mPhotoURI.toString());

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();

            //or perhaps do a dialog should only use one method i.e. toast or dialog, but have both code here for demo purposes
            //also I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Image saved successfully.").setPositiveButton("OK", null).show();
        } else {
            Toast t = Toast.makeText(this, "Error taking photo", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    /**
     * Returns the Email Body Message.
     * <p> Email body message is created used prescription related data inputed from user </p>
     *
     * @return Email Body Message
     */
    private String createOrderSummary()
    {
        String orderMessage = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();
        orderMessage += "\n" + "\n" + getString(R.string.order_message_1);
        String optionalInstructions = meditOptional.getText().toString();

        orderMessage += "\n" + getString(R.string.order_message_collect) + ((CharSequence) mSpinner.getSelectedItem()).toString() + " days";
        orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();
        return orderMessage;
    }

    public void sendEmail(View v)
    {

        //check that Name is not empty, and ask do they want to continue
        Log.i(TAG, "Email Attachment: " + mPhotoURI.toString());

        String customerName = mCustomerName.getText().toString();
        if (customerName.matches(""))
        {
            Toast.makeText(this, getString(R.string.customer_name_blank), Toast.LENGTH_SHORT).show();

            /* we can also use a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Customer Name not set.").setPositiveButton("OK", null).show();
            */
        } else
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.to_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_STREAM, mPhotoURI);
            intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
            if (intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        }
    }


}
