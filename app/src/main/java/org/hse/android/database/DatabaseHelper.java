package org.hse.android.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableEntity;
import org.hse.android.utils.Converters;

@Database(entities = {GroupEntity.class, TeacherEntity.class, TimeTableEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseHelper extends RoomDatabase {

    public static final String DATABASE_NAME = "hse_timetable";

    public abstract HseDao hseDao();
}
