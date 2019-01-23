package com.sda.mcmullm2.assignment4;

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
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

public class FragmentProducts extends Fragment {

  /**
   * This method creates an {@link ArrayList} of {@link Product} objects (some repeated for
   * illustration) and links it to the GridView via a {@link ProductAdapter}.
   * <p>
   * Each item is also given its own click event handler to display a {@link Toast} message, along
   * with a simple animation.
   *
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_products, container, false);

    // Create an ArrayList of Product objects
    final ArrayList<Product> products = new ArrayList<>();
    products.add(new Product("Polo Shirt", 50.00, R.drawable.ic_product_polo));
    products.add(new Product("Loose Fit", 20.00, R.drawable.ic_product_mens));
    products.add(new Product("Ladies", 40.00, R.drawable.ic_product_ladies));
    products.add(new Product("Long Sleeve", 60.00, R.drawable.ic_product_longsleeve));
    products.add(new Product("V-Neck", 40.00, R.drawable.ic_product_vneck));
    products.add(new Product("Sports", 60.00, R.drawable.ic_product_sports));
    products.add(new Product("Hoodies", 70.00, R.drawable.ic_product_hoodie));
    products.add(new Product("Baby Tees", 15.00, R.drawable.ic_product_baby));
    products.add(new Product("Polo Shirt", 50.00, R.drawable.ic_product_polo));
    products.add(new Product("Loose Fit", 20.00, R.drawable.ic_product_mens));
    products.add(new Product("Ladies", 40.00, R.drawable.ic_product_ladies));
    products.add(new Product("Long Sleeve", 60.00, R.drawable.ic_product_longsleeve));
    products.add(new Product("V-Neck", 40.00, R.drawable.ic_product_vneck));
    products.add(new Product("Sports", 60.00, R.drawable.ic_product_sports));
    products.add(new Product("Hoodies", 70.00, R.drawable.ic_product_hoodie));


    // Create an {@link ProductAdapter}, whose data source is a list of {@link Product}s. The
    // adapter knows how to create views for each item in the list.
    final ProductAdapter flavorAdapter = new ProductAdapter(getActivity(), products);


    // Get a reference to the GridView, and attach the adapter to the GridView.
    GridView listView = view.findViewById(R.id.listview_products);
    listView.setAdapter(flavorAdapter);

    // Add event listener to each item.
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
        Product selectedItem = products.get(i);
        selectedItem.toggleSelection();

        String toastMsg = selectedItem.getProductName() + " ";
        toastMsg += selectedItem.isSelected() ? "Added" : "Removed";
        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();

        // Animate the selected item.
        final int animationDuration = 200;
        view.animate().setInterpolator(new AccelerateInterpolator(0.8f))
            .setDuration(animationDuration).translationY(-100).withEndAction(new Runnable() {
          @Override
          public void run() {
            view.animate().setInterpolator(new BounceInterpolator())
                .setDuration(animationDuration * 4).translationY(0);
          }
        });

        // Change the background colour to indicate selection.
        // Alternatively, we could use flavorAdapter.notifyDataSetChanged();, as the code below is
        // essentially a copy of what's found in the adapter. However, this also prevents the
        // animation from working, and appears to be less responsive to user clicks. From the docs:
        // https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter#notifyDataSetChanged()
        // "...it will always be more efficient to use the more specific change events if you can.
        // Rely on notifyDataSetChanged() as a last resort."
        ImageView image = view.findViewById(R.id.list_item_icon);
        if (image != null) {
          int bgColour = selectedItem.isSelected() ? ContextCompat.getColor(view.getContext(), R.color.product_item_background_selected) : ContextCompat.getColor(view.getContext(), R.color.product_item_background);
          image.setBackgroundColor(bgColour);
        }
      }
    });

    return view;
  }

}
