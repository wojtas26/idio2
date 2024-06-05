package cz.idio;

import android.annotation.SuppressLint;
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
import cz.idio.shift.EmployeeService;
import cz.idio.shift.Request;
import cz.idio.shift.Shift;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeFragment extends Fragment {

    private Context mContext;
    private EditText shiftIdEditText, noteEditText, requestTypeEditText, requestDateEditText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        // Inicializace UI prvků
        shiftIdEditText = view.findViewById(R.id.shiftIdEditText);
        noteEditText = view.findViewById(R.id.noteEditText);
        requestTypeEditText = view.findViewById(R.id.requestTypeEditText);
        requestDateEditText = view.findViewById(R.id.requestDateEditText);
        Button confirmShiftButton = view.findViewById(R.id.confirmShiftButton);
        Button rejectShiftButton = view.findViewById(R.id.rejectShiftButton);
        Button createRequestButton = view.findViewById(R.id.createRequestButton);

        // Nastavení listenerů pro tlačítka
        confirmShiftButton.setOnClickListener(v -> confirmShift());
        rejectShiftButton.setOnClickListener(v -> rejectShift());
        createRequestButton.setOnClickListener(v -> createRequest());

        return view;
    }

    private void confirmShift() {
        int shiftId = Integer.parseInt(shiftIdEditText.getText().toString());
        String note = noteEditText.getText().toString();

        Shift shift = new Shift();
        shift.setId(shiftId);
        shift.setStatus("confirmed");
        shift.setNote(note);

        EmployeeService employeeService = ApiClient.getClient(mContext).create(EmployeeService.class);
        Call<Void> call = employeeService.confirmShift(shift);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Shift confirmed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to confirm shift", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectShift() {
        int shiftId = Integer.parseInt(shiftIdEditText.getText().toString());
        String note = noteEditText.getText().toString();

        Shift shift = new Shift();
        shift.setId(shiftId);
        shift.setStatus("rejected");
        shift.setNote(note);

        EmployeeService employeeService = ApiClient.getClient(mContext).create(EmployeeService.class);
        Call<Void> call = employeeService.rejectShift(shift);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Shift rejected successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to reject shift", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRequest() {
        String requestType = requestTypeEditText.getText().toString();
        String requestDate = requestDateEditText.getText().toString();

        Request request = new Request();
        request.setEmployeeId("employeeId"); // Získat ID zaměstnance z přihlášení nebo jiného zdroje
        request.setType(requestType);
        request.setDate(requestDate);
        request.setStatus("pending");

        EmployeeService employeeService = ApiClient.getClient(mContext).create(EmployeeService.class);
        Call<Void> call = employeeService.createRequest(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Request created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to create request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
