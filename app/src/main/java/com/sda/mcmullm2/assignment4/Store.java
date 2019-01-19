package com.sda.mcmullm2.assignment4;

public class Store {
  private String area;
  private String address;
  private String phone;

  public Store(String area, String address, String phone) {
    setArea(area);
    setAddress(address);
    setPhone(phone);
  }

  public String getArea() {
    return area;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public void setArea(String area) {
    this.area = area.trim();
  }

  public void setAddress(String address) {
    this.address = address.trim();
  }

  public void setPhone(String phone) {
    this.phone = phone.trim();
  }
}
