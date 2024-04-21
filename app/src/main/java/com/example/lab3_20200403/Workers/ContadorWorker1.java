package com.example.lab3_20200403.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ContadorWorker1 extends Worker {
    public ContadorWorker1(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        for (int i = 1; i <= 20; i++) {

            Log.d("msg-test-i", "i: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return Result.failure();
                //throw new RuntimeException(e);
            }
        }

        return Result.success();
    }
}
