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

        displayPrescriptions();

        returnToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrescriptionListActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayPrescriptions() {

        PrescriptionDatabaseHelper dbHelper = new PrescriptionDatabaseHelper(this);
        List<Prescription> prescriptions = dbHelper.getAllPrescriptions();


        PrescriptionAdapter adapter = new PrescriptionAdapter(this, prescriptions, dbHelper);
        prescriptionListView.setAdapter(adapter);
    }


}
