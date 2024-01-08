package com.kadirsoran.medicinereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "prescriptions.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "prescriptions";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DOSAGE = "dosage";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_DURATION = "duration";
    private Context context;
    public PrescriptionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;  // Add this line
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DOSAGE + " TEXT, " +
                COLUMN_FREQUENCY + " TEXT, " +
                COLUMN_DURATION + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public long savePrescription(Prescription prescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, prescription.getPrescriptionName());
        values.put(COLUMN_DOSAGE, prescription.getDosage());
        values.put(COLUMN_FREQUENCY, prescription.getFrequency());
        values.put(COLUMN_DURATION, prescription.getDuration());

        long insertedId = db.insert(TABLE_NAME, null, values);

        db.close();
        return insertedId;
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String dosage = cursor.getString(cursor.getColumnIndex(COLUMN_DOSAGE));
                String frequency = cursor.getString(cursor.getColumnIndex(COLUMN_FREQUENCY));
                String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));

                prescriptions.add(new Prescription(name, dosage, frequency, duration));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return prescriptions;
    }

    public void deletePrescription(String prescriptionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{prescriptionName});
        db.close();
    }
    public void scheduleReminder(String prescriptionName, long reminderTimeMillis, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra("prescriptionName", prescriptionName);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
        }
    }

}
