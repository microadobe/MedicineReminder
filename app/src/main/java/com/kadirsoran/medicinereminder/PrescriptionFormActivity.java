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
        String prescriptionName = prescriptionNameEditText.getText().toString().trim();
        String dosage = dosageEditText.getText().toString().trim();

        if (prescriptionName.isEmpty() || dosage.isEmpty()) {
            Toast.makeText(this, "Please enter prescription details", Toast.LENGTH_SHORT).show();
            return;
        }
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

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        notificationTime.set(Calendar.MINUTE, minute);
        notificationTime.set(Calendar.SECOND, 0);


        scheduleNotification(prescriptionId, notificationTime.getTimeInMillis());
        Toast.makeText(this, "Prescription saved successfully", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void scheduleNotification(long prescriptionId, long startTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) prescriptionId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, startTimeMillis, pendingIntent);
    }



}
