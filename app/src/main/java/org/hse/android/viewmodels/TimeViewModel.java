package org.hse.android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.hse.android.database.HseRepository;

import java.util.Date;

public class TimeViewModel extends AndroidViewModel {
    private final HseRepository repository;

    public TimeViewModel(@NonNull Application application) {
        super(application);
        repository = new HseRepository(application);
    }

    public void setDateTime() {
        repository.setDateTime();
    }

    public Date getDateTime() {
        return repository.getDateTime().getValue();
    }
}
