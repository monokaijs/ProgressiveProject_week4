package com.monokaijs.progressive1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageViewer extends Fragment {
  public static ImageViewer instance;
  public static int currentIndex = 0;

  public ImageViewer() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ImageViewer.instance = this;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_image_viewer, container, false);

    rootView.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // go to next image
        setImageIndex(1);
      }
    });

    rootView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // go to previous image
        setImageIndex(-1);
      }
    });
    return rootView;
  }

  public void setImageIndex(int navigateIndex) {
    ImageView imageView = MainActivity.instance.findViewById(R.id.imageView);
    if (MainActivity.imageList.size() <= 0) {
      Toast.makeText(MainActivity.instance, "No image available", Toast.LENGTH_SHORT).show();
      return;
    }
    if (imageView == null) {
      Log.i("ACTIVITY", "IMAGE VIEW NOT AVAILABLE");
      return;
    }
    currentIndex = currentIndex + navigateIndex;

    // image rotation
    // if over last index -> go to first index
    // if under first index -> go to last index
    if (currentIndex > MainActivity.imageList.size() - 1) {
      currentIndex = 0;
    } else if (currentIndex < 0) {
      currentIndex = MainActivity.imageList.size() - 1;
    }
    Log.i("INDEX", String.valueOf(currentIndex));

    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(MainActivity.instance);
    circularProgressDrawable.setStrokeWidth(5f);
    circularProgressDrawable.setCenterRadius(30f);
    circularProgressDrawable.start();

    StoredImage item = MainActivity.imageList.get(currentIndex);

    // load image, showing a circular progress as indicator
    Glide.with(this)
        .load(item.url)
        .placeholder(circularProgressDrawable)
        .into(imageView);
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.i("ACTIVITY", "ON_START");
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.i("ACTIVITY", "RESUME");
    if (MainActivity.imageList.size() > 0) {
      setImageIndex(0);
    }
  }
}