package cz.idio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cz.idio.api.ApiClient;
import cz.idio.shift.AdminService;
import cz.idio.shift.Shift;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFragment extends Fragment {

    private Context mContext;
    private EditText employeeIdEditText, dateEditText, startTimeEditText, endTimeEditText;
    private Button createShiftButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        // Inicializace UI prvků
        employeeIdEditText = view.findViewById(R.id.employeeIdEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        startTimeEditText = view.findViewById(R.id.startTimeEditText);
        endTimeEditText = view.findViewById(R.id.endTimeEditText);
        createShiftButton = view.findViewById(R.id.createShiftButton);

        // Nastavení listeneru pro tlačítko
        createShiftButton.setOnClickListener(v -> createShift());

        return view;
    }

    private void createShift() {
        String employeeId = employeeIdEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String startTime = startTimeEditText.getText().toString();
        String endTime = endTimeEditText.getText().toString();

        if (employeeId.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(mContext, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Shift shift = new Shift();
        shift.setEmployeeId(employeeId);
        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.setStatus("pending");

        AdminService adminService = ApiClient.getClient(mContext).create(AdminService.class);
        Call<Void> call = adminService.createShift(shift);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Shift created successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(mContext, "Failed to create shift", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        employeeIdEditText.setText("");
        dateEditText.setText("");
        startTimeEditText.setText("");
        endTimeEditText.setText("");
    }
}
