/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sda.mcmullm2.assignment3;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * {@link FlavorActivity} shows a list of Android platform releases.
 * For each release, display the name, version number, and image.
 */
public class FlavorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavor);

        // Create an ArrayList of Product objects
        final ArrayList<Product> products = new ArrayList<Product>();
        products.add(new Product("Donut", "1.6", R.drawable.donut));
        products.add(new Product("Eclair", "2.0-2.1", R.drawable.eclair));
        products.add(new Product("Froyo", "2.2-2.2.3", R.drawable.froyo));
        products.add(new Product("GingerBread", "2.3-2.3.7", R.drawable.gingerbread));
        products.add(new Product("Honeycomb", "3.0-3.2.6", R.drawable.honeycomb));
        products.add(new Product("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.icecream));
        products.add(new Product("Jelly Bean", "4.1-4.3.1", R.drawable.jellybean));
        products.add(new Product("KitKat", "4.4-4.4.4", R.drawable.kitkat));
        products.add(new Product("Lollipop", "5.0-5.1.1", R.drawable.lollipop));
        products.add(new Product("Marshmallow", "6.0-6.0.1", R.drawable.marshmallow));

        // Create an {@link AndroidFlavorAdapter}, whose data source is a list of
        // {@link Product}s. The adapter knows how to create list item views for each item
        // in the list.
        AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(this, products);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.listview_flavor);
        listView.setAdapter(flavorAdapter);

        // Add event listener to each item.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                Product selectedItem = products.get(i);
                String toastMsg = selectedItem.getProductName();
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();

                // Not required. Just animating the selected item.
                final int animationDuration = 200;
                view.animate().setInterpolator(new AccelerateInterpolator(0.8f)).setDuration(animationDuration).translationY(-100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().setInterpolator(new BounceInterpolator()).setDuration(animationDuration * 4).translationY(0);
                    }
                });
            }
        });
    }

}

