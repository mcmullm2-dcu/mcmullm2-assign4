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

/**
 * {@link Product} represents a single product type.
 * Each object has 3 properties: name, price, and image resource ID.
 */
public class Product {

    // Name of the product (e.g. Polo T-Shirt, etc)
    private String productName;

    // Price of the product
    private String productPrice;

    // Drawable resource ID
    private int productResourceId;

    /**
    * Create a new Product object.
    *
    * @param name is the name of the product (e.g. Polo T-Shirt)
    * @param price is the corresponding price
    * @param resourceId is drawable reference ID that corresponds to the product
    * */
    public Product(String name, String price, int resourceId)
    {
        productName = name;
        productPrice = price;
        productResourceId = resourceId;
    }

    /**
     * Get the name of the product
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get the product price
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * Get the image resource ID
     */
    public int getImageResourceId() {
        return productResourceId;
    }


}
