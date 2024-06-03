package cz.idio.WorkManager;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import cz.idio.api.ApiClient;
import cz.idio.shift.EmployeeService;
import cz.idio.shift.Shift;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncWorker extends Worker {

    private Context mContext;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Implementace synchronizace dat
        syncData();
        return Result.success();
    }

    private void syncData() {
        // Příklad synchronizace směn
        EmployeeService employeeService = ApiClient.getClient(mContext).create(EmployeeService.class);
        Call<List<Shift>> call = employeeService.getShifts();
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                if (response.isSuccessful()) {
                    List<Shift> shifts = response.body();
                    // Aktualizace lokální databáze nebo UI s novými daty
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                // Chyba při synchronizaci
            }
        });
    }
}

