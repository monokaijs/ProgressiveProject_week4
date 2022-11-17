package com.monokaijs.progressive1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageDataHandler extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "ImageLinks";
  private static final int DATABASE_VERSION = 1;
  private static final String IMAGES_TABLE_NAME = "app_images";
  private static final String KEY_ID = "id";
  private static final String KEY_TYPE = "type";
  private static final String KEY_URL = "url";
  private static final String KEY_LOCATION = "location";

  public ImageDataHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @SuppressLint("DefaultLocale")
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(String.format(
        "CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)",
        IMAGES_TABLE_NAME, KEY_ID, KEY_TYPE, KEY_URL, KEY_LOCATION
    ));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(
        String.format("DROP TABLE IF EXISTS %s", IMAGES_TABLE_NAME)
    );

    onCreate(db);
  }

  public void addImage(StoredImage image) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_TYPE, image.type);
    values.put(KEY_URL, image.url);
    values.put(KEY_LOCATION, image.location);
    db.insert(IMAGES_TABLE_NAME, null, values);
    db.close();
  }


  public List<StoredImage> getAllImages() {
    List<StoredImage> tripList = new ArrayList<>();
    String query = "SELECT * FROM " + IMAGES_TABLE_NAME;

    SQLiteDatabase db = this.getReadableDatabase();
    @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
    cursor.moveToFirst();

    while (!cursor.isAfterLast()) {
      StoredImage trip = new StoredImage(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
      tripList.add(trip);
      cursor.moveToNext();
    }
    Log.i("DB", String.valueOf(tripList.size()));
    return tripList;
  }
  public void deleteImage(int tripId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(IMAGES_TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(tripId)});
    db.close();
  }

  public void deleteAllImages() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL(
        String.format("DROP TABLE IF EXISTS %s", IMAGES_TABLE_NAME)
    );
  }
}
