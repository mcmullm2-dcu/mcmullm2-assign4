package com.sda.mcmullm2.assignment4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class FragmentCollection extends Fragment {
  private SharedPreferences prefs;
  private Store selected = null;
  private boolean isCollected = false;

  /**
   * Determines if the order is to be collected.
   */
  public boolean isForCollection() {
    return isCollected;
  }

  /**
   * Get the collection address, if any.
   *
   * @return
   */
  public String getAddress() {
    if (selected != null) {
      return formatAddress(selected);
    }
    return "";
  }

  public void setPreferences(SharedPreferences prefs) {
    this.prefs = prefs;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_collection, container, false);

    // Create an ArrayList of stores available for collection
    final ArrayList<Store> stores = new ArrayList<Store>();
    stores.add(new Store("Swords", "1 Main Street, Swords, Co. Dublin", "01 234 5678"));
    stores.add(new Store("Galway", "5 Eyre Square, Galway", "091 234 5678"));
    stores.add(new Store("Cork", "54 Patrick's Street, Cork", "021 456 9543"));
    stores.add(new Store("Wexford", "21 Roe Street, Wexford", "053 654 2348"));
    stores.add(new Store("Dun Laoghaire", "75 Upper Georges Street, Dun Laoghaire, Co. Dublin", "01 987 2646"));

    // Create a {@link StoreAdapter}, whose data source is a list of {@link Store}s. The
    // adapter knows how to create views for each item in the list.
    final StoreAdapter adapter = new StoreAdapter(getActivity(), stores);

    // Get a reference to the ListView, and attach the adapter to the ListView.
    final ListView listView = view.findViewById(R.id.listview_stores);
    listView.setAdapter(adapter);

    // Add event listener to each item.
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
        Store clickedStore = stores.get(i);
        if (selected != clickedStore) {
          selected = stores.get(i);
          isCollected = true;
          view.setSelected(true);
          String toastMsg = String.format(getString(R.string.collection_selected), selected.getArea());
          Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
        } else {
          view.setSelected(false);
          listView.setItemChecked(-1, true);
          adapter.notifyDataSetChanged();
          selected = null;
          isCollected = false;
          Toast.makeText(getActivity(), "Off", Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }

  public String formatAddress(Store store) {
    StringBuilder sb = new StringBuilder();
    sb.append(store.getArea()).append(": ");
    sb.append(store.getAddress()).append(".\n");
    sb.append(getString(R.string.order_message_phone)).append(": ");
    sb.append(store.getPhone());
    return sb.toString();
  }
}
