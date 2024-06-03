package cz.idio.WorkManager;

import android.content.Context;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class SyncScheduler {

    public static void scheduleSync(Context context) {
        PeriodicWorkRequest syncWorkRequest = new PeriodicWorkRequest.Builder(SyncWorker.class, 1, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context).enqueue(syncWorkRequest);
    }
}
