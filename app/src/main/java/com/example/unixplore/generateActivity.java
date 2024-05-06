package com.example.unixplore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.unixplore.BuildConfig;

public class generateActivity extends AppCompatActivity {

    private TextView generatedSOPTextView;
    private Button pdfDownloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        generatedSOPTextView = findViewById(R.id.generatedSOPTextView);
        pdfDownloadButton = findViewById(R.id.pdfDownloadButton);

        String generatedSOP = getIntent().getStringExtra("generatedSOP");
        generatedSOPTextView.setText(generatedSOP);

        pdfDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAndSavePDF(generatedSOP);
            }
        });
    }

    private void downloadAndSavePDF(String generatedSOP) {
        File pdfFile = createPDFFile(generatedSOP);
        if (pdfFile != null) {
            viewPDF(pdfFile);
        } else {
            Log.e("PDF_ERROR", "Failed to create PDF file.");
        }
    }

    private File createPDFFile(String generatedSOP) {
        File pdfFile = null;
        Document document = new Document();

        try {
            File dir = new File(getExternalFilesDir(null), "MyApplication");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("PDF_ERROR", "Failed to create directory: " + dir.getAbsolutePath());
                    return null;
                }
            }

            pdfFile = new File(dir, "generated_sop.pdf");
            pdfFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(document, fos);
            document.open();

            document.add(new Paragraph(generatedSOP));

            document.close();
            fos.close();

            String pdfPath = pdfFile.getAbsolutePath();
            Log.d("PDF_PATH", "PDF saved to: " + pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PDF_ERROR", "Exception occurred: " + e.getMessage());
            return null;
        }

        return pdfFile;
    }

    private void viewPDF(File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(pdfUri, "application/pdf");
        startActivity(intent);
    }
}
