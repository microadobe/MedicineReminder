package com.kadirsoran.medicinereminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PrescriptionAdapter extends ArrayAdapter<Prescription> {

    private Context context;
    private List<Prescription> prescriptions;
    private PrescriptionDatabaseHelper dbHelper;
    private void testNotificationForPrescription(Prescription prescription) {

        Intent intent = new Intent(context, ReminderService.class);

        intent.putExtra("prescriptionName", prescription.getPrescriptionName());

        context.startService(intent);
    }


    public PrescriptionAdapter(Context context, List<Prescription> prescriptions,PrescriptionDatabaseHelper dbHelper) {
        super(context, 0, prescriptions);
        this.context = context;
        this.prescriptions = prescriptions;
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.prescription_list_item, parent, false);
        }

        Prescription currentPrescription = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.textPrescriptionName);
        nameTextView.setText(currentPrescription.getPrescriptionName());

        TextView dosageTextView = listItemView.findViewById(R.id.textDosage);
        dosageTextView.setText("Dosage: " + currentPrescription.getDosage());

        TextView frequencyTextView = listItemView.findViewById(R.id.textFrequency);
        frequencyTextView.setText("Frequency: " + currentPrescription.getFrequency());

        TextView durationTextView = listItemView.findViewById(R.id.textDuration);
        durationTextView.setText("Duration: " + currentPrescription.getDuration());

        Button deleteMedicineButton = listItemView.findViewById(R.id.buttonDeleteMedicine);
        deleteMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.deletePrescription(currentPrescription.getPrescriptionName());
                Toast.makeText(getContext(), "Deleted: " + currentPrescription.getPrescriptionName(), Toast.LENGTH_SHORT).show();

            }
        });




        Button testNotificationButton = listItemView.findViewById(R.id.buttonTestNotification);
        testNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Testing notification for: " + currentPrescription.getPrescriptionName(), Toast.LENGTH_SHORT).show();
                testNotificationForPrescription(currentPrescription);

            }
        });

        return listItemView;
    }
}
