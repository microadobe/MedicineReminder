package com.kadirsoran.medicinereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userEmailTextView;
    private Button addPrescriptionButton;
    private Button viewTimedPrescriptionsButton;
    private Button viewSaglikBakanligiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userEmailTextView = findViewById(R.id.userEmailTextView);
        addPrescriptionButton = findViewById(R.id.addPrescriptionButton);
        viewTimedPrescriptionsButton = findViewById(R.id.viewTimedPrescriptionsButton);
        viewSaglikBakanligiButton = findViewById(R.id.viewSaglikBakanligiButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            if (userEmail != null) {
                userEmailTextView.setText("Email: " + userEmail);
            }
        }

        addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open PrescriptionFormActivity for adding prescriptions
                startActivity(new Intent(UserProfileActivity.this, PrescriptionFormActivity.class));
            }
        });

        viewTimedPrescriptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, PrescriptionListActivity.class));
            }
        });
        viewSaglikBakanligiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, SaglikBakanligi.class));
            }
        });
    }
}
