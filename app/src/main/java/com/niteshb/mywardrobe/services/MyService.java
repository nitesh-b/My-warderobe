package com.niteshb.mywardrobe.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.niteshb.mywardrobe.models.ItemModel;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyService extends Service {
    public MyService() {
    }

    private IBinder binder = new MyBinder();

    public class MyBinder extends Binder{
        public MyService getMyService(){
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return binder;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(Realm.getDefaultInstance() == null){
            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name("wardrobe.realm")
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
            Realm.setDefaultConfiguration(config);
        }

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }


    public void performTask(){
        FutureTask<String> task = new FutureTask<>(new BackgroundTask(), "backgroundTask");
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(task);
    }

}