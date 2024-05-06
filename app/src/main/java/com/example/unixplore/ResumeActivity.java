package com.example.unixplore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResumeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CardView cardResume;
    CardView cardSOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Resume and SOP");

        setContentView(R.layout.activity_resume);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        cardResume = findViewById(R.id.card_resume);
        cardSOP = findViewById(R.id.card_sop);

        bottomNavigationView.setSelectedItemId(R.id.menu_resume);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        cardResume.setOnClickListener(v -> {
            startActivity(new Intent(ResumeActivity.this, Mainnn.class));
        });

        cardSOP.setOnClickListener(v -> {
            startActivity(new Intent(ResumeActivity.this, Main.class));
        });

    }
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            startActivity(new Intent(ResumeActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        } else if (itemId == R.id.menu_search) {
            startActivity(new Intent(ResumeActivity.this, SearchActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        } else if (itemId == R.id.menu_resume) {
            return true;
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(ResumeActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return false;
    }
}




