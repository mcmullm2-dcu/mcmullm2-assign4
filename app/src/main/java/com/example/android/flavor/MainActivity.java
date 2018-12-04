package com.example.android.flavor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity
{

    static private final String TAG = "Assign 3";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Launches the FlavourActivity, to display data in list format
     *
     * @param view EditText
     */
    public void callList(View view)
    {
        Intent intent = new Intent(this, FlavorActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    /**
     * Launches the OrderActivity, to capture Order Information
     *
     * @param view EditText
     */
    public void callOrder(View view)
    {
        Intent intent = new Intent(this, OrderActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }


}

