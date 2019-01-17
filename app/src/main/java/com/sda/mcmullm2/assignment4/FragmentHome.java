package com.sda.mcmullm2.assignment4;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentHome extends Fragment implements OnClickListener {
  private final int TAB_PRODUCTS_INDEX = 1;
  private final int TAB_ORDER_INDEX = 2;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // super.onCreateView(inflater, container, savedInstanceState);

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    // Assign click event listeners to the home page buttons.
    Button productButton = view.findViewById(R.id.product_button);
    Button orderButton = view.findViewById(R.id.order_button);
    productButton.setOnClickListener(this);
    orderButton.setOnClickListener(this);

    return view;
  }

  /**
   * Click event handler for the home page buttons to navigate to other tabs.
   *
   * <a href="https://stackoverflow.com/a/14571018/5233918">Click events in fragments</a>
   * <a href="https://stackoverflow.com/a/50072628/5233918">Programmatically changing TABS</a>
   *
   * @param view The View that was clicked to trigger this method (a button in this case)
   */
  @Override
  public void onClick(View view) {
    TabLayout tabs = getActivity().findViewById(R.id.tab_layout);
    if (tabs != null && tabs.getTabCount() > 0) {
      switch (view.getId()) {
        case R.id.product_button:
          tabs.getTabAt(TAB_PRODUCTS_INDEX).select();
          break;
        case R.id.order_button:
          tabs.getTabAt(TAB_ORDER_INDEX).select();
          break;
      }
    }
  }
}
