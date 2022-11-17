package com.monokaijs.progressive1;

import java.util.Objects;

public class StoredImage {
  public String type = "url";
  public String url = "";
  public String storedUrl = "";
  public String location = "";

  public StoredImage(String newType, String url) {
    this.type = newType;
    if (Objects.equals(newType, "url")) {
      this.url = url;
    } else {
      this.storedUrl = url;
    }
  }
}
