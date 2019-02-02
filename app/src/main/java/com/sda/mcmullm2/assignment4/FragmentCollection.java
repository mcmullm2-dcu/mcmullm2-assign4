package com.sda.mcmullm2.assignment4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
  private static final String TAG = "Assign4";
  private static SharedPreferences prefs;
  private Store selected = null;
  private boolean isCollected = false;
  final ArrayList<Store> stores = new ArrayList<>();
  private ListView lv = null;
  private int selectedIndex = -1;

  /**
   * Default empty constructor
   */
  public FragmentCollection() {
  }

  /**
   * Factory method to create a new instance of FragmentCollection
   * @param preferences
   * @return
   */
  public static FragmentCollection newInstance(SharedPreferences preferences) {
    FragmentCollection fragment = new FragmentCollection();
    prefs = preferences;
    return fragment;
  }

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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_collection, container, false);

    // Create an ArrayList of stores available for collection
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
    lv = listView;
    restorePreferences();
    listView.setAdapter(adapter);

    // Add event listener to each item.
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
        Store clickedStore = stores.get(i);
        if (selected != clickedStore) {
          listView.setSelector(R.color.product_item_background_selected);
          setSelected(listView, i);
          selected = stores.get(i);
          selectedIndex = i;
          isCollected = true;
          String toastMsg = String.format(getString(R.string.collection_selected), selected.getArea());
          Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
        } else {
          view.setSelected(false);
          listView.setSelector(R.color.light_accent);
          setSelected(listView, -1);
          selected = null;
          isCollected = false;
          Toast.makeText(getActivity(), getString(R.string.collection_off), Toast.LENGTH_SHORT).show();
        }
        savePreferences();
      }
    });

    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
    if (lv != null) {
      state.putInt("storeIndex", lv.getSelectedItemPosition());
    }
    if (selected != null) {
      state.putString("area", selected.getArea());
    }
    state.putBoolean("isCollected", isCollected);
  }

    public String formatAddress(Store store) {
    StringBuilder sb = new StringBuilder();
    sb.append(store.getArea()).append(": ");
    sb.append(store.getAddress()).append(".\n");
    sb.append(getString(R.string.order_message_phone)).append(": ");
    sb.append(store.getPhone());
    return sb.toString();
  }

  /**
   * Saves the state of a selected item to Shared Preferences.
   */
  public void savePreferences() {
    if (prefs != null) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean("IsCollection", isCollected);
      editor.putString("CollectionArea", selected != null ? selected.getArea() : "");
      editor.putString("CollectionAddress", selected != null ? getAddress() : "");
      editor.putInt("CollectionIndex", selected != null ? selectedIndex : -1);
      editor.commit();
    }
  }

  /**
   * Restore data from Shared Preferences.
   */
  public void restorePreferences() {
    if (prefs != null && lv != null) {
      isCollected = prefs.getBoolean("IsCollection", false);
      selectedIndex = prefs.getInt("CollectionIndex", -1);
      String area = prefs.getString("CollectionArea", "");
      if (area != "" && stores != null) {
        for (Store s : stores) {
          if (s.getArea() == area) {
            selected = s;
            setSelected(lv, selectedIndex);
            break;
          }
        }
      }
    }
  }

  public void setSelected(ListView listView, int index) {
    listView.setSelector(index >= 0 ? R.color.product_item_background_selected : R.color.light_accent);
    listView.setSelection(index);
  }
}
