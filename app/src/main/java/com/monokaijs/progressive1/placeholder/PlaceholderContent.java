package com.monokaijs.progressive1.placeholder;

import com.monokaijs.progressive1.MainActivity;
import com.monokaijs.progressive1.StoredImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PlaceholderContent {
  public static final List<StoredImage> ITEMS = MainActivity.imageList;
  public static final Map<String, String> ITEM_MAP = new HashMap<String, String>();

  private static final int COUNT = 25;

  private static void addItem(StoredImage item) {
    ITEMS.add(item);
  }

  private static String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position);
    for (int i = 0; i < position; i++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }
}