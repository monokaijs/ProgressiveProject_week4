package com.monokaijs.progressive1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.monokaijs.progressive1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  public final String APP_TAG = "ProgressiveProject";
  private static final int MY_PERMISSION_REQUEST_CAMERA = 101;
  public String photoFileName = "photo.jpg";

  File photoFile;
  public static MainActivity instance;
  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;
  public static ImageDataHandler db;
  public static List<StoredImage> imageList = new ArrayList<>();

  AlertDialog alertDialog;
  String[] permissionsStr = {Manifest.permission.CAMERA,
      Manifest.permission.RECORD_AUDIO,
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION
  };
  int permissionsCount = 0;
  ActivityResultLauncher<String[]> permissionsLauncher =
      registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
          new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String,Boolean> result) {

            }
          });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MainActivity.instance = this;

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    db = new ImageDataHandler(this.getBaseContext());
    MainActivity.imageList = db.getAllImages();

    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_add_link) {
      getNavController().navigate(R.id.action_ImageViewerFragment_to_addLink);
    }
    if (id == R.id.action_all_links) {
      getNavController().navigate(R.id.action_ImageViewerFragment_to_imageLinkFragment);
    }
    if (id == R.id.capture_new) {
      ArrayList<String> permissionsList = new ArrayList<>(Arrays.asList(permissionsStr));
      askForPermissions(permissionsList);
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        getNavController().navigate(R.id.action_ImageViewerFragment_to_captureNewPicture);
      }
    }
    return super.onOptionsItemSelected(item);
  }

  public NavController getNavController() {
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
    if (!(fragment instanceof NavHostFragment)) {
      throw new IllegalStateException("Activity " + this + " does not have a NavHostFragment");
    }
    return ((NavHostFragment) fragment).getNavController();
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  private void askForPermissions(ArrayList<String> permissionsList) {
    String[] newPermissionStr = new String[permissionsList.size()];
    for (int i = 0; i < newPermissionStr.length; i++) {
      newPermissionStr[i] = permissionsList.get(i);
    }
    if (newPermissionStr.length > 0) {
      permissionsLauncher.launch(newPermissionStr);
    } else {
      showPermissionDialog();
    }
  }

  private void showPermissionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Permission required")
        .setMessage("Some permissions are need to be allowed to use this app without any problems.")
        .setPositiveButton("Settings", (dialog, which) -> {
          dialog.dismiss();
        });
    if (alertDialog == null) {
      alertDialog = builder.create();
      if (!alertDialog.isShowing()) {
        alertDialog.show();
      }
    }
  }
}