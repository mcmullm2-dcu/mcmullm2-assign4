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
 * {@link ProductActivity} shows a list of products.
 * For each product, display the name, price, and image.
 */
public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flavor);

        // Create an ArrayList of Product objects
        final ArrayList<Product> products = new ArrayList<Product>();
        products.add(new Product("Polo Shirt", 50.00, R.drawable.ic_product_polo));
        products.add(new Product("Loose Fit", 20.00, R.drawable.ic_product_mens));
        products.add(new Product("Ladies", 40.00, R.drawable.ic_product_ladies));
        products.add(new Product("Long Sleeve", 60.00, R.drawable.ic_product_longsleeve));
        products.add(new Product("V-Neck", 40.00, R.drawable.ic_product_vneck));
        products.add(new Product("Sports", 60.00, R.drawable.ic_product_sports));
        products.add(new Product("Hoodies", 70.00, R.drawable.ic_product_hoodie));
        products.add(new Product("Baby Tees", 15.00, R.drawable.ic_product_baby));

        // Create an {@link ProductAdapter}, whose data source is a list of
        // {@link Product}s. The adapter knows how to create list item views for each item
        // in the list.
        ProductAdapter flavorAdapter = new ProductAdapter(this, products);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = findViewById(R.id.listview_flavor);
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
