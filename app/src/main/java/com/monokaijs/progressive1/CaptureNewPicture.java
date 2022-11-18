package com.monokaijs.progressive1;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaptureNewPicture extends Fragment {
  static final int REQUEST_IMAGE_CAPTURE = 1;
  Button btnCapture;
  Button btnSave;
  ImageView imgView;
  String currentFilePath;
  int LOCATION_REFRESH_TIME = 15000;
  int LOCATION_REFRESH_DISTANCE = 500;
  Location currentLocation;
  LocationManager mLocationManager;

  private final LocationListener mLocationListener = new LocationListener() {
    @Override
    public void onLocationChanged(final Location location) {
      currentLocation = location;
    }
  };

  public static CaptureNewPicture newInstance(String param1, String param2) {
    CaptureNewPicture fragment = new CaptureNewPicture();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mLocationManager = (LocationManager) MainActivity.instance.getSystemService(LOCATION_SERVICE);

    if (ActivityCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
      LOCATION_REFRESH_DISTANCE, mLocationListener);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    View root = inflater.inflate(R.layout.fragment_capture_new_picture, container, false);

    btnCapture = root.findViewById(R.id.btnCapture);
    btnSave = root.findViewById(R.id.btnSave);
    imgView = root.findViewById(R.id.imgPreview2);

    btnSave.setEnabled(false);

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String addressString = "";
        if (currentLocation != null) {
          Address address = getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
          assert address != null;
          addressString = address.getSubAdminArea() + ", " + address.getCountryName() + ", " + address.getPostalCode();
        }
        StoredImage storedImg = new StoredImage("stored", currentFilePath, addressString);
        MainActivity.db.addImage(storedImg);
        MainActivity.imageList.add(storedImg);
        Toast.makeText(MainActivity.instance, "New Picture Stored", Toast.LENGTH_SHORT).show();
        // MainActivity.instance.getNavController().navigate(R.id.action_addLink_to_ImageViewerFragment);
      }
    });

    btnCapture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dispatchCamera();
      }
    });

    return root;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i("RESULT", "ON_RESULT");
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        Log.i("RESULT", "CAPTURED");
        assert data != null;
        Bitmap bitmapData = (Bitmap) data.getExtras().get("data");
        imgView.setImageBitmap(bitmapData);
        btnSave.setEnabled(true);
        Date date = new Date();
        String path = savePhotoToExternalStorage(String.valueOf(date.getTime()), bitmapData);
        currentFilePath = path;
        Log.i("OUTPUT", path);
      }
    }
  }

  private String savePhotoToExternalStorage(String name, Bitmap bmp) {
    File pictureFile = getOutputMediaFile(name);
    if (pictureFile == null) {
      return "";
    }
    try {
      FileOutputStream fos = new FileOutputStream(pictureFile);
      bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
      fos.close();
    } catch (FileNotFoundException e) {
      Log.d("ERROR", "Not found: " + e.getMessage());
    } catch (IOException e) {
      Log.d("ERROR", "Accessing file: " + e.getMessage());
    }

    return pictureFile.getAbsolutePath();
  }

  private File getOutputMediaFile(String name) {
    File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
      + "/Android/data/"
      + MainActivity.instance.getApplicationContext().getPackageName()
      + "/Files");
    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return null;
      }
    }
    File mediaFile;
    String storedFileName = "MI_" + name + ".jpg";
    return new File(mediaStorageDir.getPath() + File.separator + storedFileName);
  }

  public void dispatchCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
  }

  private Address getAddress(double latitude, double longitude) {
    MainActivity mContext = MainActivity.instance;
    try {
      if (mContext == null) return null;
      Geocoder gc = new Geocoder(mContext);
      List<Address> list = gc.getFromLocation(latitude, longitude, 1);
      if (list != null && list.size() > 0) return list.get(0);
    } catch (Exception e) {
      Log.e("ERROR", e.getMessage());
    }
    return null;
  }
}