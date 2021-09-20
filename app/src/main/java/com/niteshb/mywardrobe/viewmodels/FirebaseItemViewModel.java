package com.niteshb.mywardrobe.viewmodels;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.niteshb.mywardrobe.models.ItemModel;
import com.niteshb.mywardrobe.repositories.ItemRepository;
import com.niteshb.mywardrobe.services.MyService;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseItemViewModel  extends ViewModel {

    private MutableLiveData<MyService.MyBinder> myBinderMutableLiveData = new MutableLiveData<>();
    private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
        myBinderMutableLiveData.postValue(myBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    myBinderMutableLiveData.postValue(null);
    }
};

    public MutableLiveData<MyService.MyBinder> getMyBinder() {
        return myBinderMutableLiveData;
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }
}
