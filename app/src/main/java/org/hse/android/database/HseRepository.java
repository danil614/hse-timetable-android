package org.hse.android.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableWithTeacherEntity;

import java.util.Date;
import java.util.List;

public class HseRepository {
    private final HseDao dao;

    public HseRepository(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getHseDao();
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return dao.getAllGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return dao.getAllTeachers();
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date) {
        return dao.getTimeTableTeacher();
    }


    // По дате и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndGroupId(Date date, int id) {
        return dao.getTimeTableByDateAndGroupId(date, id);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndTeacherId(Date date, int id) {
        return dao.getTimeTableByDateAndTeacherId(date, id);
    }


    //  По временному промежутку и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableStudentInRange(Date startDate, Date endDate, int id) {
        return dao.getTimeTableStudentInRange(startDate, endDate, id);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherInRange(Date startDate, Date endDate, int id) {
        return dao.getTimeTableTeacherInRange(startDate, endDate, id);
    }
}
