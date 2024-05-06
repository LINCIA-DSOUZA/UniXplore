
package com.example.unixplore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private ProgressBar loadingProgressBar;

    Button submitbutton;
    LinearLayout layout;
    LinearLayout main_content;
    Button Edit;

    Dialog dialog;



    private final ArrayList<String> countryList = new ArrayList<>(Arrays.asList("Australia", "Canada", "Germany", "U.K.", "U.S.A"));
    private final ArrayList<String> testList = new ArrayList<>(Arrays.asList("TOEFL",
            "No test given",
            "IELTS",
            "Duolingo",
            "GMAT",
            "GRE",
            "SAT",
            "ACT",
            "PTE"));
    private final ArrayList<String> streamList = new ArrayList<>(Arrays.asList("Engineering",
            "Business",
            "Psychology",
            "Information Studies",
            "Journalism",
            "Finance",
            "Law",
            "Architecture",
            "Data Science",
            "Medical"));

    private final ArrayList<String> durList = new ArrayList<>(Arrays.asList("Years","Months"));
   //SharedPrefsManager sharedpref;
   private ArrayList<String> loadCsvData() {
       ArrayList<String> courseList = new ArrayList<>();
       BufferedReader reader = null;

       try {
           // Improved resource handling using getIdentifier and openRawResource
           @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier("courselist", "raw", getPackageName());
           InputStream inputStream = getResources().openRawResource(resourceId);
           reader = new BufferedReader(new InputStreamReader(inputStream));

           String line;
           while ((line = reader.readLine()) != null) {
               // Split the line using comma as delimiter
               String[] courses = line.split(",");
               // Add each course string (trimmed) to the ArrayList
               for (String course : courses) {
                   courseList.add(course.trim());
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (reader != null) {
               try {
                   reader.close();
               } catch (IOException e) {
                   // handle close exception (optional)
               }
           }
       }

       return courseList;
   }


    @SuppressLint("DiscouragedApi")
    private int getResId(String resName) {
        return getResources().getIdentifier(resName, "raw", getPackageName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedpref = new SharedPrefsManager(this);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Recommendations");

        setContentView(R.layout.activity_home);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        loadingProgressBar.setVisibility(View.GONE);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        EditText editText1 = (EditText) findViewById(R.id.pref5);
        EditText editText2 = (EditText) findViewById(R.id.pref6);
        EditText editText3 = (EditText) findViewById(R.id.pref8);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<String> courseList = loadCsvData(); // Replace with your CSV file names (without .csv)


        // Create and populate your spinner adapters
        MySpinnerAdapter adapter1 = new MySpinnerAdapter(countryList);
        Spinner spinner1 = (Spinner) findViewById(R.id.pref1);
        spinner1.setAdapter(adapter1);

//        MySpinnerAdapter adapter2 = new MySpinnerAdapter(streamList);
//        Spinner spinner2 = (Spinner) findViewById(R.id.pref2);
//        spinner2.setAdapter(adapter2);

//AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
//        MySpinnerAdapter adapter3 = new MySpinnerAdapter(streamList);
//        Spinner spinner3 = (Spinner) findViewById(R.id.pref3);
//        spinner3.setAdapter(adapter3);

        MySpinnerAdapter adapter4 = new MySpinnerAdapter(testList);
        Spinner spinner4 = (Spinner) findViewById(R.id.pref4);
        spinner4.setAdapter(adapter4);


        MySpinnerAdapter adapter5 = new MySpinnerAdapter(durList);
        Spinner spinner5 = (Spinner) findViewById(R.id.pref7);
        spinner5.setAdapter(adapter5);


        submitbutton = findViewById(R.id.submit_button);
        Edit = findViewById(R.id.edit_form);
        Edit.setVisibility(View.GONE);
        layout = findViewById(R.id.form_container);
        main_content = findViewById(R.id.main_content);

//        SharedPrefsManager.UserData userdata = sharedpref.loadData();
//        if(sharedpref!=null){
//            System.out.println("Data Load");
//            loadUserData(userdata,editText1,editText2,editText3,spinner1,spinner2,spinner3,spinner4,spinner5);
//        }

        TextView textview = findViewById(R.id.testView);

        textview.setOnClickListener(v -> {
            // Initialize dialog
            dialog=new Dialog(HomeActivity.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // set custom height and width
            dialog.getWindow().setLayout(650,800);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            // Initialize and assign variable
            EditText editText=dialog.findViewById(R.id.edit_text);
            ListView listView=dialog.findViewById(R.id.list_view);

            // Initialize array adapter
            ArrayAdapter<String> adapter=new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1,courseList);

            // set adapter
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // when item selected from list
                    // set selected item on textView
                    textview.setText(adapter.getItem(position));

                    // Dismiss dialog
                    dialog.dismiss();
                }
            });
        });

        TextView textview2 = findViewById(R.id.testView2);

        textview2.setOnClickListener(v -> {
            // Initialize dialog
            dialog=new Dialog(HomeActivity.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // set custom height and width
            dialog.getWindow().setLayout(650,800);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            // Initialize and assign variable
            EditText editText=dialog.findViewById(R.id.edit_text);
            ListView listView=dialog.findViewById(R.id.list_view);

            // Initialize array adapter
            ArrayAdapter<String> adapter=new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1,streamList);

            // set adapter
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // when item selected from list
                    // set selected item on textView
                    textview2.setText(adapter.getItem(position));

                    // Dismiss dialog
                    dialog.dismiss();
                }
            });
        });




        submitbutton.setOnClickListener(v -> {

            layout.setVisibility(View.GONE);
            Edit.setVisibility(View.VISIBLE);
            String country;
            country = (String) spinner1.getSelectedItem();
            if(country.equals("U.K.")){
                country = "UK";
            }else if(country.equals("U.S.A")){
                country = "USA";

            }

//            String course = (String) spinner2.getSelectedItem();
            String course = (String) textview.getText();
//            String stream = (String) spinner3.getSelectedItem();
            String stream = (String) textview2.getText();
            String test = (String) spinner4.getSelectedItem();
            int duration = Integer.parseInt(editText2.getText().toString());
            float dur_year;
            String year_month = (String) spinner5.getSelectedItem();
            if(year_month.equals("Months")){
                dur_year = (float) duration/12;
            }else{
                dur_year = duration;
            }
            float score = Float.parseFloat(editText1.getText().toString());
            int budget = Integer.parseInt(editText3.getText().toString());
            System.out.println(country);
            System.out.println(stream);
            System.out.println("Will make request");
            makeRequest(country,course,stream,test, dur_year,score, String.valueOf(duration),year_month,budget);


        });


        Edit.setOnClickListener(v -> {

            layout.setVisibility(View.VISIBLE);
            Edit.setVisibility(View.GONE);
        });


    }

//    public void setSpinner(String pref, Spinner spinner) {
//        int position = spinner.getAdapter() != null ? ((MySpinnerAdapter) spinner.getAdapter()).getPosition(pref) : -1;
//        if (position != -1) {
//            spinner.setSelection(position);
//        } else {
//            spinner.setSelection(0);
//        }
//    }
//    public void loadUserData(SharedPrefsManager.UserData userdata,EditText editText1, EditText editText2, EditText editText3, Spinner spinner1, Spinner spinner2, Spinner spinner3,Spinner spinner4,Spinner spinner5){
//
//            editText1.setText(userdata.score);
//            editText2.setText(userdata.duration);
//            editText3.setText(userdata.fees);
//            setSpinner(userdata.country, spinner1);
//            setSpinner(userdata.course,spinner2);
//            setSpinner(userdata.stream,spinner3);
//            setSpinner(userdata.test,spinner4);
//            setSpinner(userdata.mo_yr,spinner5);
//
//    }


    public void makeRequest(String country,String course,String stream,String test, float dur_year ,float score,String duration,String year_month,int budget){
        OkHttpClient Client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Adjust the timeout period as needed
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


//
        System.out.println("request made");
        System.out.println(country);
        System.out.println(stream);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("course", course);
            jsonObject.put("stream", stream);
            jsonObject.put("country",country);
            jsonObject.put("test",test);
            jsonObject.put("score",score);
            jsonObject.put("duration",dur_year);
            jsonObject.put("fee",budget);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody reqbody =  RequestBody.create(jsonObject.toString(),mediaType);

        Request request = new Request.Builder().url("http:///top_10_universities").post(reqbody).build();


        // Show loading screen
        loadingProgressBar.setVisibility(View.VISIBLE);

        Client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(() -> {
                    // Update UI here
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG);
                    toast.show();
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                runOnUiThread(() -> {
                    // Update UI here
                    loadingProgressBar.setVisibility(View.GONE);
                    assert response.body() != null;
                    try {

                        String resp = response.body().string();

//                        sharedpref.saveData(country, course, stream, test, String.valueOf(score), duration, year_month, Integer.toString(budget), () -> {
//                            // Optional actions after successful data save (e.g., Toast message)
//                            Toast toast = Toast.makeText(getApplicationContext(),"Preferences saved",Toast.LENGTH_LONG);
//                            toast.show();
                            parseJson(resp);
//                        });

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

    }

    public void parseJson(String jsonData) {
        jsonData = jsonData.replace("\\", "");
        if (jsonData.startsWith("\"")) {
            jsonData = jsonData.substring(1);
        }

        if (jsonData.endsWith("\"")) {
            jsonData = jsonData.substring(0, jsonData.length() - 1);
        }
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonData));
            reader.setLenient(true); // Enable lenient mode
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Access keys and values within the nested objects
            for (String university : jsonObject.keySet()) {
                JsonObject innerObject = jsonObject.getAsJsonObject(university);
                String imageUrl = innerObject.get("image").getAsString();
                String test = innerObject.get("test").getAsString(); // Extracting test
                String testScore = innerObject.get("test_score").getAsString(); // Extracting test_score
                String acceptanceRate = innerObject.get("acceptance_rate").getAsString(); // Extracting acceptance_rate
                String desc = innerObject.get("description").getAsString();
                System.out.println("University: " + university);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("Test: " + test);
                System.out.println("Test Score: " + testScore);
                System.out.println("Acceptance Rate: " + acceptanceRate);

                // Extracting additional information
                JsonArray coursesArray = innerObject.getAsJsonArray("courses");
                JsonArray durationArray = innerObject.getAsJsonArray("duration");
                JsonArray streamArray = innerObject.getAsJsonArray("stream");
                JsonArray feesArray = innerObject.getAsJsonArray("fees");

                // Convert JsonArrays to String arrays
                String[] courses = new String[coursesArray.size()];
                String[] duration = new String[durationArray.size()];
                String[] stream = new String[streamArray.size()];
                String[] fees = new String[feesArray.size()];

                for (int i = 0; i < coursesArray.size(); i++) {
                    courses[i] = coursesArray.get(i).getAsString();
                }
                for (int i = 0; i < durationArray.size(); i++) {
                    duration[i] = durationArray.get(i).getAsString();
                }
                for (int i = 0; i < streamArray.size(); i++) {
                    stream[i] = streamArray.get(i).getAsString();
                }
                for (int i = 0; i < feesArray.size(); i++) {
                    fees[i] = feesArray.get(i).getAsString();
                }

                makeCard(university, imageUrl, test, testScore, acceptanceRate, fees, courses, stream, duration,desc);
            }
        } catch (JsonSyntaxException e) {
            // Log any errors that occur during parsing
            e.printStackTrace();
        }
    }





    public void makeCard(String uniName, String imageUrl, String acceptanceRate, String test, String testScore, String[] fees, String[] courses, String[] stream, String[] duration,String desc) {
        LinearLayout mainLayout = findViewById(R.id.main_content);
        @SuppressLint("InflateParams") View cardView = getLayoutInflater().inflate(R.layout.card_layout, null);
        ImageView imageView = cardView.findViewById(R.id.Uni_imageView);
        TextView universityNameTextView = cardView.findViewById(R.id.Uni_textView);

        // Set university name and image
        universityNameTextView.setText(uniName);
        Picasso.get().load(imageUrl).into(imageView);

        // Add an OnClickListener to handle card clicks
        cardView.setOnClickListener(v -> {
            // Handle card click here
            // For example, open a detail activity or perform some action
            Intent intent = new Intent(getApplicationContext(), UniInfoActivity.class);
            intent.putExtra("uni_name", uniName);
            intent.putExtra("uni_image", imageUrl);
            intent.putExtra("uni_acceptance_rate", acceptanceRate);
            intent.putExtra("uni_test", test);
            intent.putExtra("uni_test_score", testScore);
            intent.putExtra("uni_fees", fees);
            intent.putExtra("uni_courses", courses);
            intent.putExtra("uni_stream", stream);
            intent.putExtra("uni_dur", duration);
            intent.putExtra("uni_desc",desc);
            startActivity(intent);
        });

        mainLayout.addView(cardView);
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        cardView.startAnimation(slideUp);
    }


    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            return true;
        } else if (itemId == R.id.menu_search) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        } else if (itemId == R.id.menu_resume) {
            startActivity(new Intent(HomeActivity.this, ResumeActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return false;
    }
}