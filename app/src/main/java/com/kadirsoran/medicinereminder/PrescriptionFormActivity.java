package com.kadirsoran.medicinereminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PrescriptionFormActivity extends AppCompatActivity {

    private EditText prescriptionNameEditText;
    private EditText dosageEditText;
    private EditText reminderTimeEditText;
    private EditText reminderDurationEditText;
    private Button saveButton;

    private Calendar selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_form);

        prescriptionNameEditText = findViewById(R.id.prescriptionNameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        reminderTimeEditText = findViewById(R.id.reminderTimeEditText);
        reminderDurationEditText = findViewById(R.id.reminderDurationEditText);
        saveButton = findViewById(R.id.saveButton);

        selectedTime = Calendar.getInstance();

        reminderTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        reminderDurationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrescription();
            }
        });
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        reminderTimeEditText.setText(sdf.format(selectedTime.getTime()));
                    }
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedTime.set(Calendar.YEAR, year);
                        selectedTime.set(Calendar.MONTH, month);
                        selectedTime.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd mm yyyy", Locale.getDefault());
                        reminderDurationEditText.setText(sdf.format(selectedTime.getTime()));
                    }
                },
                selectedTime.get(Calendar.YEAR),
                selectedTime.get(Calendar.MONTH),
                selectedTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void savePrescription() {
        // Get prescription details
        String prescriptionName = prescriptionNameEditText.getText().toString().trim();
        String dosage = dosageEditText.getText().toString().trim();

        // Check if prescription details are valid
        if (prescriptionName.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(this, "Please enter prescription details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save prescription details to the database
        Prescription prescription = new Prescription(
                prescriptionName,
                dosage,
                reminderTimeEditText.getText().toString(),
                reminderDurationEditText.getText().toString()
        );

        PrescriptionDatabaseHelper dbHelper = new PrescriptionDatabaseHelper(this);
        long prescriptionId = dbHelper.savePrescription(prescription);
        String[] timeParts = prescription.getFrequency().split(":");
        int hourOfDay = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Create a Calendar instance for the specified time
        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        notificationTime.set(Calendar.MINUTE, minute);
        notificationTime.set(Calendar.SECOND, 0);



        // Schedule the notification
        scheduleNotification(prescriptionId, notificationTime.getTimeInMillis());
        // Display success message
        Toast.makeText(this, "Prescription saved successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity
        finish();
    }
    private long parseDuration(String duration) {
        // Implement logic to parse the user-input duration and convert it to milliseconds
        // For example, you can parse hours and minutes and convert them to milliseconds
        // Adjust this logic based on your expected input format (e.g., "3 hours 30 minutes")
        // This is a basic example and might need adjustments based on your requirements
        // For simplicity, I'm assuming the duration is in the format "X hours Y minutes"

        long totalMillis = 0;

        if (duration != null && !duration.isEmpty()) {
            String[] parts = duration.split(" ");
            totalMillis += Integer.parseInt(parts[1]) * 60 * 60 * 1000;
            totalMillis += Integer.parseInt(parts[0]) * 60 * 1000;

        }

        return totalMillis;
    }
    private void scheduleNotification(long prescriptionId, long startTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to be triggered by the alarm
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("prescriptionId", prescriptionId); // Pass prescriptionId to the receiver

        // Create a PendingIntent for the alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) prescriptionId, // Use prescriptionId as the requestCode
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT // Use FLAG_UPDATE_CURRENT to update the existing PendingIntent
        );

        // Schedule a one-time alarm for each day at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, startTimeMillis, pendingIntent);
    }


    private long calculateDuration() {
        // Implement your logic to calculate the duration in milliseconds
        // This could be based on user input or a fixed duration, depending on your requirements
        // For simplicity, I'm returning a fixed value of 24 hours in milliseconds as an example
        return 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    }
}
