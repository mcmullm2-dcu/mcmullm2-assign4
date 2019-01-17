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
 * - New JavaDoc comments.
 * - Refactored code to meet Google Java Style Guide:
 *   - https://google.github.io/styleguide/javaguide.html
 *   - Replaces Hungarian notation.
 *   - Uses two-space indents.
 */

package com.sda.mcmullm2.assignment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * {@link MainActivity} is the first Activity displayed when the application is launched, featuring
 * two buttons to navigate to the other available Activities.
 */
public class MainActivity extends AppCompatActivity {

  /**
   * An identifying tag used for filtering log messages, if required.
   */
  private static final String TAG = "Assign 3";

  /**
   * Called when {@link MainActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_main);
  }

  /**
   * Launches the {@link ProductActivity}, to display data in a grid format.
   *
   * @param view The View that was clicked to trigger this method (a button in this case)
   */
  public void callList(View view) {
    Intent intent = new Intent(this, ProductActivity.class);
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    }
  }

  /**
   * Launches the {@link OrderActivity}, to capture Order Information.
   *
   * @param view The View that was clicked to trigger this method (a button in this case)
   */
  public void callOrder(View view) {
    Intent intent = new Intent(this, OrderActivity.class);
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    }
  }


}

