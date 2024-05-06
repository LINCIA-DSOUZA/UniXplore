package com.example.unixplore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import java.io.IOException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class userinput extends AppCompatActivity {

    private EditText editTextDegree, editTextMajor, editTextUniversity, editTextRelevantSkills, editareas, editprogrammingLanguages, editprojectsInternships, editparticipatedIn, editachievements, editorganization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinput);

        editTextDegree = findViewById(R.id.editTextDegree);
        editTextMajor = findViewById(R.id.editTextMajor);
        editTextUniversity = findViewById(R.id.editTextUniversity);
        editTextRelevantSkills = findViewById(R.id.editTextRelevantSkills);
        editareas = findViewById(R.id.editareas);
        editprogrammingLanguages = findViewById(R.id.editprogrammingLanguages);
        editprojectsInternships = findViewById(R.id.editprojectsInternships);
        editparticipatedIn = findViewById(R.id.editparticipatedIn);
        editachievements = findViewById(R.id.editachievements);
        editorganization = findViewById(R.id.editorganization);


        Button buttonGenerateSOP = findViewById(R.id.buttonGenerateSOP);
        buttonGenerateSOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateSOP();
            }
        });
    }

    private void generateSOP() {
        String degree = editTextDegree.getText().toString();
        String major = editTextMajor.getText().toString();
        String university = editTextUniversity.getText().toString();
        String relevantSkills = editTextRelevantSkills.getText().toString();
        String areas = editareas.getText().toString();
        String programmingLanguages = editprogrammingLanguages.getText().toString();
        String projectsInternships = editprojectsInternships.getText().toString();
        String participatedIn = editparticipatedIn.getText().toString();
        String achievements = editachievements.getText().toString();
        String organization = editorganization.getText().toString();

        Intent intent = getIntent();
        String selectedProfession = intent.getStringExtra("selectedProfession");


        List<String> sopTemplates = readSOPTemplatesFromCSV(selectedProfession);

        String generatedSOP = generateSOPFromTemplate(sopTemplates, degree, major, university, relevantSkills, areas, programmingLanguages, projectsInternships, participatedIn, achievements, organization);


        Intent displayIntent = new Intent(userinput.this, generateActivity.class);
        displayIntent.putExtra("generatedSOP", generatedSOP);
        startActivity(displayIntent);
    }

    private List<String> readSOPTemplatesFromCSV(String selectedProfession) {
        List<String> sopTemplates = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.soptemplates);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(inputStreamReader);

            String[] headers = csvReader.readNext();
            int professionIndex = -1;
            int sopIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("profession")) {
                    professionIndex = i;
                } else if (headers[i].equalsIgnoreCase("sop")) {
                    sopIndex = i;
                }
            }

            if (professionIndex == -1 || sopIndex == -1) {
                return sopTemplates;
            }

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                String profession = nextLine[professionIndex].trim();
                if (profession.equalsIgnoreCase(selectedProfession)) {
                    String sopTemplate = nextLine[sopIndex].trim();
                    sopTemplates.add(sopTemplate);
                }
            }

            csvReader.close();

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return sopTemplates;
    }



    private String generateSOPFromTemplate(List<String> sopTemplates, String degree, String major, String university, String relevantSkills, String areas, String programmingLanguages, String projectsInternships, String participatedIn, String achievements, String organization) {
        StringBuilder generatedSOPBuilder = new StringBuilder();


        if (!sopTemplates.isEmpty()) {
            String sopTemplate = sopTemplates.get(0);


            sopTemplate = sopTemplate.replace("[degree]", degree)
                    .replace("[major]", major)
                    .replace("[university]", university)
                    .replace("[relevantSkills]", relevantSkills)
                    .replace("[areas]", areas)
                    .replace("[programmingLanguages]", programmingLanguages)
                    .replace("[projectsInternships]", projectsInternships)
                    .replace("[participatedIn]", participatedIn)
                    .replace("[achievements]", achievements)
                    .replace("[organization]", organization);

            generatedSOPBuilder.append(sopTemplate);
        }

        return generatedSOPBuilder.toString();
    }
}