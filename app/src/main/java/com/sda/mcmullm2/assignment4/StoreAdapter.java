package com.sda.mcmullm2.assignment4;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class StoreAdapter extends ArrayAdapter<Store> {

  public StoreAdapter(Activity context, ArrayList<Store> stores) {
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // The second argument is used when the ArrayAdapter is populating a single TextView.
    // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, stores);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Check if the existing view is being reused, otherwise inflate the view
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.store_item, parent, false);
    }

    // Get the Product object located at this position in the list
    Store currentStore = getItem(position);

    // Find the TextView in the store_item.xml layout with the ID store_area.
    TextView areaTextView = listItemView.findViewById(R.id.store_area);

    // Get the area from the current Store object and set this text on the area TextView.
    areaTextView.setText(currentStore.getArea());

    // Find the TextView in the store_item.xml layout with the ID store_address.
    TextView addressTextView = listItemView.findViewById(R.id.store_address);

    // Get the area from the current Store object and set this text on the address TextView.
    addressTextView.setText(currentStore.getAddress());

    // Find the TextView in the store_item.xml layout with the ID store_phone.
    TextView phoneTextView = listItemView.findViewById(R.id.store_phone);

    // Get the area from the current Store object and set this text on the address TextView.
    phoneTextView.setText(currentStore.getPhone());

    // Return the whole list item layout (containing 3 TextViews) so that it can be shown in the ListView
    return listItemView;
  }
}

