package com.example.unixplore;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UniInfoActivity extends AppCompatActivity {
    // Initialize RecyclerViews for array values (fees, courses, stream, duration)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uni_info_activity);

        // Initialize views
        ImageView imageView = findViewById(R.id.uni_imageView);
        TextView uniNameTextView = findViewById(R.id.uni_nameTextView);
        TextView acceptanceRateTextView = findViewById(R.id.acceptanceRateTextView);
        TextView testTextView = findViewById(R.id.testTextView);
        TextView testScoreTextView = findViewById(R.id.testScoreTextView);
        TextView descTextView = findViewById(R.id.uni_desc);
        // Initialize RecyclerViews for array values

        // Get intent data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String uniName = extras.getString("uni_name");
            String imageUrl = extras.getString("uni_image");
            String acceptanceRate = extras.getString("uni_acceptance_rate");
            String test = extras.getString("uni_test");
            String testScore = extras.getString("uni_test_score");
            String[] fees = extras.getStringArray("uni_fees");
            String[] courses = extras.getStringArray("uni_courses");
            String[] stream = extras.getStringArray("uni_stream");
            String[] duration = extras.getStringArray("uni_dur");
            String desc = extras.getString("uni_desc");
            // Set data to views
            uniNameTextView.setText(uniName);
            acceptanceRateTextView.setText("Acceptance Rate: " + testScore);
            testTextView.setText("Test: " + acceptanceRate );
            testScoreTextView.setText("Test Score: " +test );
            descTextView.setText(desc);

            // Load image using Picasso
            Picasso.get().load(imageUrl).into(imageView);

            RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
            RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
            RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
            RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
// Create and set adapter for each RecyclerView
            MyAdapter adapter1 = new MyAdapter(courses);
            recyclerView1.setAdapter(adapter1);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView1.setLayoutManager(layoutManager1);

            MyAdapter adapter2 = new MyAdapter(stream);
            recyclerView2.setAdapter(adapter2);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView2.setLayoutManager(layoutManager2);

            MyAdapter adapter3 = new MyAdapter(duration);
            recyclerView3.setAdapter(adapter3);
            LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView3.setLayoutManager(layoutManager3);

            MyAdapter adapter4 = new MyAdapter(fees);
            recyclerView4.setAdapter(adapter4);
            LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView4.setLayoutManager(layoutManager4);

        }
    }
}