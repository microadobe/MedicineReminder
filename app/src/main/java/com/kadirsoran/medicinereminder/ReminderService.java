package com.kadirsoran.medicinereminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.Manifest;

public class ReminderService extends IntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Retrieve prescription details from the intent
        String prescriptionName = intent.getStringExtra("prescriptionName");

        // Display a notification for the reminder
        showNotification(prescriptionName);
    }

    private void showNotification(String prescriptionName) {
        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "medication_reminder_channel";
            CharSequence channelName = "Medication Reminder Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "medication_reminder_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Medicine Reminder")
                .setContentText("Time to take your " + prescriptionName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Display the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        notificationManager.notify(1, builder.build());
    }
}
