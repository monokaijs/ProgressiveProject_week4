package com.monokaijs.progressive1;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.monokaijs.progressive1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;

  // Create list of images
  public static List<String> imageList = Arrays.asList(
      "https://i.imgur.com/FhKzmRn_d.webp?maxwidth=800&fidelity=grand",
      "https://i.imgur.com/LRoLTlK.jpeg",
      "https://i.imgur.com/1ofnU.jpeg",
      "https://i.imgur.com/LIlqion.jpeg",
      "https://i.imgur.com/9BU396Q.jpeg"
  );
  public static int currentIndex = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    setImageIndex(0);

    binding.getRoot().findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // go to next image
        setImageIndex(1);
      }
    });

    binding.getRoot().findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // go to previous image
        setImageIndex(-1);
      }
    });
  }

  private void setImageIndex(int navigateIndex) {
    ImageView imageView = findViewById(R.id.imageView);
    currentIndex = currentIndex + navigateIndex;

    // image rotation
    // if over last index -> go to first index
    // if under first index -> go to last index
    if (currentIndex > imageList.size() - 1) {
      currentIndex = 0;
    } else if (currentIndex < 0) {
      currentIndex = imageList.size() - 1;
    }
    Log.i("INDEX", String.valueOf(currentIndex));

    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
    circularProgressDrawable.setStrokeWidth(5f);
    circularProgressDrawable.setCenterRadius(30f);
    circularProgressDrawable.start();
    // load image, showing a circular progress as indicator
    Glide.with(this)
        .load(imageList.get(currentIndex))
        .placeholder(circularProgressDrawable)
        .into(imageView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}