package cz.idio.WorkManager;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import cz.idio.shift.Shift;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SyncWorker extends Worker {

    private DatabaseReference mDatabase;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public Result doWork() {
        // Implementace synchronizace dat
        syncData();
        return Result.success();
    }

    private void syncData() {
        mDatabase.child("shifts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shift shift = snapshot.getValue(Shift.class);
                    // Aktualizace lokální databáze nebo UI s novými daty
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Chyba při synchronizaci
            }
        });
    }
}
