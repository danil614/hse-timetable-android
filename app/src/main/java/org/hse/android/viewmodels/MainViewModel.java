package org.hse.android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.hse.android.database.HseRepository;
import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;
import org.hse.android.schedulefiles.ScheduleMode;
import org.hse.android.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final HseRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new HseRepository(application);
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return repository.getGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return repository.getTeachers();
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date) {
        return repository.getTimeTableTeacherByDate(date);
    }


    // По дате и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndGroupId(Date date, int groupId) {
        return repository.getTimeTableByDateAndGroupId(date, groupId);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndTeacherId(Date date, int groupId) {
        return repository.getTimeTableByDateAndTeacherId(date, groupId);
    }


    // По временному промежутку и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableForDay(Date currentDate, int id, ScheduleMode mode) {
        Date[] dayBoundary = DateUtils.getDayBoundary(currentDate);
        // с начала текущего дня
        Date startDate = dayBoundary[0];
        // до конца этого дня
        Date endDate = dayBoundary[1];

        switch (mode) {
            case STUDENT:
                return repository.getTimeTableStudentInRange(startDate, endDate, id);
            case TEACHER:
                return repository.getTimeTableTeacherInRange(startDate, endDate, id);
        }
        return null;
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableForWeek(Date currentDate, int id, ScheduleMode mode) {
        Date[] weekBoundary = DateUtils.getWeekBoundary(currentDate);
        // с начала текущей недели
        Date startDate = weekBoundary[0];
        // до конца текущей недели
        Date endDate = weekBoundary[1];

        switch (mode) {
            case STUDENT:
                return repository.getTimeTableStudentInRange(startDate, endDate, id);
            case TEACHER:
                return repository.getTimeTableTeacherInRange(startDate, endDate, id);
        }
        return null;
    }
}
