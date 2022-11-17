package com.monokaijs.progressive1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class AddLink extends Fragment {
  public boolean valid = false;
  public String currentLink = "";
  public AddLink() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_add_link, container, false);
    ImageView imageView = rootView.findViewById(R.id.imgPreview);
    Button btnFinish = rootView.findViewById(R.id.btnFinish);
    EditText editor = rootView.findViewById(R.id.inpLink);
    btnFinish.setEnabled(false);

    btnFinish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ImageViewer.instance.imageList.add(currentLink);
      }
    });

    editor.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence currentValue, int start, int before, int count) {
        Log.i("IMAGE", currentValue.toString());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(rootView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(rootView).load(currentValue.toString())
            .addListener(new RequestListener<Drawable>() {
              @Override
              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.i("INVALID", "INVALID_RESOURCE");
                btnFinish.setEnabled(false);
                return false;
              }

              @Override
              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                btnFinish.setEnabled(true);
                currentLink = currentValue.toString();
                return false;
              }
            })
            .placeholder(circularProgressDrawable)
            .into(imageView);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });
    return rootView;
  }
}