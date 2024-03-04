package org.hse.android.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.hse.android.database.entities.GroupEntity;
import org.hse.android.database.entities.TeacherEntity;
import org.hse.android.database.entities.TimeTableEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private static final String LOG_TAG = "LOG_TAG";
    private final SimpleDateFormat dataFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private final DatabaseHelper db;

    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseManager(context.getApplicationContext());
        return instance;
    }

    public HseDao getHseDao() {
        return db.hseDao();
    }

    private DatabaseManager(Context context) {
        db = Room.databaseBuilder(context,
                        DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(() -> initData(context));
                    }
                })
                .build();
    }

    private void initData(Context context) {
        Log.i(LOG_TAG, "initData");

        // Groups
        List<GroupEntity> groups = new ArrayList<>();

        String[] educationalPrograms = {"ПИ", "БИ", "Э", "Ю", "ИЯ"};
        int[] years = {20, 21, 22, 23};
        int[] groupNumbers = {1, 2, 3};

        for (String program : educationalPrograms) {
            for (int year : years) {
                for (int number : groupNumbers) {
                    String groupName = program + "-" + year + "-" + number;
                    GroupEntity group = new GroupEntity();

                    group.id = groups.size() + 1;
                    group.name = groupName;
                    groups.add(group);
                }
            }
        }

        // Teachers
        List<TeacherEntity> teachers = new ArrayList<>();
        TeacherEntity teacher = new TeacherEntity();
        teacher.id = 1;
        teacher.fio = "Иванов Иван Иванович";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 2;
        teacher.fio = "Петров Петр Петрович";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 3;
        teacher.fio = "Сидорова Елена Викторовна";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 4;
        teacher.fio = "Козлов Алексей Дмитриевич";
        teachers.add(teacher);

        DatabaseManager.getInstance(context).getHseDao().insertTeacher(teachers);


        // Timatable
        // Сегодня
        List<TimeTableEntity> timeTables = new ArrayList<>();
        TimeTableEntity timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 1;
        timeTableEntity.cabinet = "Ауд. 505";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Математика";
        timeTableEntity.corp = "Бульвар Гагарина 37а";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString(getLessonTime(0, 0, -5));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(0, 0, 30));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 2;
        timeTableEntity.cabinet = "Ауд. 506";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Физика";
        timeTableEntity.corp = "Бульвар Гагарина 37а";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(0, 0, -5));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(0, 0, 30));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        // Завтра
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 3;
        timeTableEntity.cabinet = "Ауд. 320";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "История";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 2, 0));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 3, 20));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 4;
        timeTableEntity.cabinet = "Ауд. 210";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Английский язык";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 4;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 5;
        timeTableEntity.cabinet = "Ауд. 110";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Информационная безопасность";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 6;
        timeTableEntity.cabinet = "online";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Программирование на Java";
        timeTableEntity.corp = "ONLINE";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 7;
        timeTableEntity.cabinet = "Ауд. 210";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Математическая логика";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        // Послезавтра
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 8;
        timeTableEntity.cabinet = "Ауд. 301";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Математический анализ";
        timeTableEntity.corp = "Бульвар Гагарина 37";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(2, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(2, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 9;
        timeTableEntity.cabinet = "Ауд. 201";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Основы алгоритмизации";
        timeTableEntity.corp = "Бульвар Гагарина 37";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(2, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(2, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 10;
        timeTableEntity.cabinet = "Ауд. 110";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Теория вероятностей";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(2, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(2, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 4;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 11;
        timeTableEntity.cabinet = "Ауд. 302";
        timeTableEntity.subGroup = "Программная инженерия";
        timeTableEntity.subjName = "Системное программирование";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString(getLessonTime(2, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(2, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 5;
        timeTables.add(timeTableEntity);

        DatabaseManager.getInstance(context).getHseDao().insertTimeTable(timeTables);
    }

    private Date dateFromString(String val) {
        try {
            return dataFormat.parse(val);
        } catch (ParseException e) {
            Log.d(LOG_TAG, "dateFromString ParseException ERROR");
        }

        return null;
    }

    private String getLessonTime(int days, int hours, int minutes) {
        Calendar start = Calendar.getInstance();
        hours = 24 * days + hours;

        start.add(Calendar.HOUR, hours);
        start.add(Calendar.MINUTE, minutes);

        return dataFormat.format(start.getTime());
    }
}
