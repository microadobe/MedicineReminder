package com.kadirsoran.medicinereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.kadirsoran.medicinereminder.Prescription;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {

    private ListView prescriptionListView;
    private Button returnToMainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        prescriptionListView = findViewById(R.id.prescriptionListView);
        returnToMainMenuButton = findViewById(R.id.returnToMainMenuButton);

        // Fetch and display the list of prescriptions
        displayPrescriptions();

        // Button click listener for returning to the main menu
        returnToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrescriptionListActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish(); // Close the PrescriptionListActivity and return to the main menu
            }
        });
    }

    private void displayPrescriptions() {
        // Retrieve the list of prescriptions from the database
        PrescriptionDatabaseHelper dbHelper = new PrescriptionDatabaseHelper(this);
        List<Prescription> prescriptions = dbHelper.getAllPrescriptions();

        // Create and set the adapter for the ListView
        PrescriptionAdapter adapter = new PrescriptionAdapter(this, prescriptions, dbHelper);
        prescriptionListView.setAdapter(adapter);
    }


}
