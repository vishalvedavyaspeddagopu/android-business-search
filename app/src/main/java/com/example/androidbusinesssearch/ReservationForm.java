package com.example.androidbusinesssearch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class ReservationForm extends AppCompatDialogFragment {
    private EditText email;
    private EditText time;
    private EditText date;
    private ReservationFormInterface listener;

    Integer hours = 0, minutes = 0, year = 0, month = 0, day = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reservation_form, null);

        TextView titleView = view.findViewById(R.id.reservationTitle);
        email = view.findViewById(R.id.emailInput);
        date = view.findViewById(R.id.editTextDate);
        time = view.findViewById(R.id.editTextTime);

        if (getArguments() != null) {
            titleView.setText(getArguments().getString("businessName"));
        }

        date.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) (view1, y, m, d) -> {
                        year = y;
                        month = m;
                        day = d;
                        date.setText((m + 1) + "-" + d + "-" + y);
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        time.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hr, min) -> {
                hours = hr; minutes = min;
                time.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes));
            };

            Calendar now = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });

        builder.setView(view)
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {

                })
                .setPositiveButton("SUBMIT", (dialogInterface, i) -> listener.sendFragmentData(email.getText().toString(),
                        date.getText().toString(),
                        time.getText().toString()));

        return builder.create();
    }

    public static ReservationForm newInstance(String businessName) {
        ReservationForm f = new ReservationForm();

        // Supply business id as an argument.
        Bundle args = new Bundle();
        args.putString("businessName", businessName);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ReservationFormInterface) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in fetching reservation form data:\n" + e.toString());
        }
    }

    public interface ReservationFormInterface {
        void sendFragmentData(String email, String date, String time);
    }
}
