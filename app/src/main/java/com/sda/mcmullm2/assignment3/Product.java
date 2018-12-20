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
 *
 * =======================================================================================
 *
 * Changes to the original code by Michael McMullin:
 * - Changed the original AndroidFlavor class to Product class to better describe purpose.
 * - Refactored code to meet Google Java Style Guide:
 *   - https://google.github.io/styleguide/javaguide.html
 *   - Replaces Hungarian notation.
 *   - Uses two-space indents.
 */

package com.sda.mcmullm2.assignment3;

/**
 * {@link Product} represents a single product type. Each object has 3 properties: name, price, and
 * image resource ID.
 *
 * @author Michael McMullin
 */
public class Product {

  /**
   * Name of the product (e.g. Polo T-Shirt, etc).
   */
  private final String productName;

  /**
   * Price of the product.
   */
  private final double productPrice;

  /**
   * Drawable resource ID.
   */
  private final int productResourceId;

  /**
   * Creates a new {@link Product} object with a given name, price and resource ID (image).
   *
   * @param name The name of the product (e.g. Polo T-Shirt)
   * @param price The corresponding price
   * @param resourceId The drawable reference ID that corresponds to the product
   */
  public Product(String name, double price, int resourceId) {
    productName = name;
    productPrice = price;
    productResourceId = resourceId;
  }

  /**
   * Gets the name of the product.
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Gets the formatted product price.
   */
  public String getProductPrice() {
    return String.format("€%,.2f", productPrice);
  }

  /**
   * Gets the image resource ID.
   */
  public int getImageResourceId() {
    return productResourceId;
  }


}
