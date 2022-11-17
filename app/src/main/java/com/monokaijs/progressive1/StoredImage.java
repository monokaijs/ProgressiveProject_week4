package com.monokaijs.progressive1;

public class StoredImage {
  public int id = 0;
  public String type = "url";
  public String url = "";
  public String location = "";

  public StoredImage(int id, String newType, String url, String location) {
    this.type = newType;
    this.url = url;
    this.location = location;
  }

  public StoredImage(String newType, String url) {
    this.type = newType;
    this.url = url;
  }

  public StoredImage(String newType, String url, String location) {
    this.type = newType;
    this.url = url;
    this.location = location;
  }

  public String getSource() {
    return this.url;
  }
}
