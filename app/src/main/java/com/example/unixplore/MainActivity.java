package com.example.unixplore;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavView);

        //Default home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        //finish();

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);


    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            return true;
        } else if (itemId == R.id.menu_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        } else if (itemId == R.id.menu_resume) {
            startActivity(new Intent(MainActivity.this, ResumeActivity.class));
            return true;
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return true;
        }
        return false;
    }
}
