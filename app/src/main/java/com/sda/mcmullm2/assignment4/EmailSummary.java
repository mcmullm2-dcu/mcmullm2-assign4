package com.sda.mcmullm2.assignment4;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class EmailSummary {
  private Context context;
  private String customerName;
  private String address;
  private int deliveryTime;
  private boolean isCollection;
  private HashSet<String> selectedProducts;

  public EmailSummary(Context context, SharedPreferences prefs) {
    this.context = context;
    if (prefs != null) {
      setCustomerName(prefs.getString("CustomerName", "customer"));
      setAddress(prefs.getString("Address", "address"));
      setDeliveryTime(prefs.getInt("DeliveryTime", 0));
      setIsCollection(prefs.getBoolean("IsCollection", false));
      setSelectedProducts(prefs.getStringSet("Products", null));
    }
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getDeliveryTime() {
    return deliveryTime;
  }

  public void setDeliveryTime(int deliveryTime) {
    this.deliveryTime = deliveryTime;
  }

  public boolean getIsCollection() {
    return isCollection;
  }

  public void setIsCollection(boolean collection) {
    this.isCollection = collection;
  }

  public HashSet<String> getSelectedProducts() { return selectedProducts; }

  public void setSelectedProducts(Set<String> products) { this.selectedProducts = (HashSet<String>)products; }

  /**
   * Creates a summary of the order details, ready to use in the email message body.
   *
   * <p>Email body message is created using user-input form data.
   *
   * @return Email Body Message
   */
  @Override
  public String toString() {
    // Original code replaced with StringBuilder
    StringBuilder message = new StringBuilder();

    // Add customer's name
    message.append(context.getString(R.string.customer_name));
    message.append(": ");
    message.append(customerName);
    message.append("\n\n");

    // Introduction paragraph
    message.append(context.getString(R.string.order_message_1));
    message.append("\n\n");

    // Products
    message.append(context.getString(R.string.order_product_list));
    message.append("\n");
    if (getSelectedProducts() != null) {
      for (String product : selectedProducts) {
        message.append(product);
        message.append("\n");
      }
    }
    message.append("\n");

    // Delivery instructions
    String orderAddress = fixAddress(address);
    String daysText = context.getString(deliveryTime == 1
        ? R.string.order_message_day : R.string.order_message_days);

    if (isCollection) {
      // For collection
      message.append(context.getString(R.string.order_message_collect)).append(" ");
      message.append(deliveryTime).append(" ");
      message.append(daysText).append("\n");
      message.append(context.getString(R.string.order_message_collect_from)).append(":\n");
      message.append(orderAddress);
    } else {
      // For delivery
      message.append(context.getString(R.string.order_message_deliver)).append("\n");
      message.append(orderAddress).append("\n\n");
      message.append(context.getString(R.string.order_message_deliver_time)).append(" ");
      message.append(deliveryTime).append(" ");
      message.append(daysText);
    }

    // Sign off message
    message.append("\n\n");
    message.append(context.getString(R.string.order_message_end));
    message.append("\n");
    message.append(customerName);

    return message.toString();
  }

  /**
   * Cleans up a user's address input.
   * @param address The address entered by the user through the order form.
   * @return A formatted version of the user's address.
   */
  private String fixAddress(String address) {
    String output = address.trim();
    StringBuilder sb = new StringBuilder();

    if (!output.matches("")) {
      String[] lines = output.split("\n");

      for (String s : lines) {
        String line = s.trim();
        // Check if the line is blank, or only contains a comma / full-stop.
        if (!line.matches("^$|^,$|^\\.$")) {
          sb.append(line).append("\n");
        }
      }
    }
    output = sb.toString();

    return output.trim();
  }
}
