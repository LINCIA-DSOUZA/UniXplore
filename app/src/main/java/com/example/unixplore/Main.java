package com.example.unixplore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private Spinner professionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmain);

        professionSpinner = findViewById(R.id.professionSpinner);


        List<String> professions = readProfessionsFromCSV();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, professions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(adapter);

        professionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedProfession = (String) parent.getItemAtPosition(position);


                Intent intent = new Intent(Main.this, userinput.class);


                intent.putExtra("selectedProfession", selectedProfession);


                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<String> readProfessionsFromCSV() {
        List<String> professions = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.soptemplates);
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length > 0) {
                    professions.add(nextLine[0]);
                }
            }
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return professions;
    }
}
