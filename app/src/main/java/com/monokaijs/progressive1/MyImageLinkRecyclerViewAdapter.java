package com.monokaijs.progressive1;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monokaijs.progressive1.databinding.FragmentItemBinding;

import java.util.ArrayList;
import java.util.List;

public class MyImageLinkRecyclerViewAdapter extends RecyclerView.Adapter<MyImageLinkRecyclerViewAdapter.ViewHolder> {
  private List<StoredImage> mValues = MainActivity.imageList;

  public MyImageLinkRecyclerViewAdapter(List<StoredImage> items) {
    mValues = items;
  }
  public MyImageLinkRecyclerViewAdapter() {
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    StoredImage item = mValues.get(position);
    holder.mItem = item;
    String previewUrl = item.url;
    Glide.with(MainActivity.instance).load(previewUrl).into(holder.imgItemPreview);

    holder.mContentView.setText(previewUrl);
    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
      @SuppressLint("NotifyDataSetChanged")
      @Override
      public void onClick(View v) {
        MainActivity.imageList.remove(position);
        setItems(MainActivity.imageList);
        ImageLinkFragment.adapter.notifyDataSetChanged();
      }
    });
  }

  public void setItems(List<StoredImage> list) {
    this.mValues = list;
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView mContentView;
    public final Button btnDelete;
    public final ImageView imgItemPreview;
    public StoredImage mItem;

    public ViewHolder(FragmentItemBinding binding) {
      super(binding.getRoot());
      mContentView = binding.content;
      btnDelete = binding.btnDelete;
      imgItemPreview = binding.imgItemPreview;
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
}