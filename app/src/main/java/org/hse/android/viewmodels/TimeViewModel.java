package org.hse.android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

public class TimeViewModel extends AndroidViewModel {
    public MutableLiveData<Date> dateMutableLiveData;

    public TimeViewModel(@NonNull Application application) {
        super(application);
        dateMutableLiveData = new MutableLiveData<>();
    }
}
