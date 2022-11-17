package com.monokaijs.progressive1;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static MainActivity instance;
  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;
  public static List<String> imageList = new ArrayList<String>();

  // Create list of images

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MainActivity.instance = this;

    MainActivity.imageList.add("https://i.imgur.com/FhKzmRn_d.webp?maxwidth=800&fidelity=grand");
    MainActivity.imageList.add("https://i.imgur.com/LRoLTlK.jpeg");

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

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
      return true;
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

    Log.i("ACTIVITY", "NAVIGATE_UP");
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}