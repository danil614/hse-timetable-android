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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private static final String LOG_TAG = "LOG_TAG";

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

        DatabaseManager.getInstance(context).getHseDao().insertGroup(groups);

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
        // Первые пары
        List<TimeTableEntity> timeTables = new ArrayList<>();
        TimeTableEntity timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 1;
        timeTableEntity.cabinet = "Ауд. 505";
        timeTableEntity.subGroup = "2";
        timeTableEntity.subjName = "Математика";
        timeTableEntity.corp = "Бульвар Гагарина 37а";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = getLessonTime(0, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(0, 0, 50);
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 2;
        timeTableEntity.cabinet = "Ауд. 506";
        timeTableEntity.subGroup = "1";
        timeTableEntity.subjName = "Физика";
        timeTableEntity.corp = "Бульвар Гагарина 37а";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = getLessonTime(0, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(0, 0, 50);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        // Вторые пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 3;
        timeTableEntity.cabinet = "Ауд. 506";
        timeTableEntity.subGroup = "1";
        timeTableEntity.subjName = "Физика";
        timeTableEntity.corp = "Бульвар Гагарина 37а";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = getLessonTime(0, 1, 10);
        timeTableEntity.timeEnd = getLessonTime(0, 2, 30);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        // Завтра
        // Первые пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 4;
        timeTableEntity.cabinet = "Ауд. 320";
        timeTableEntity.subGroup = "3";
        timeTableEntity.subjName = "История";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = getLessonTime(1, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(1, 0, 50);
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 5;
        timeTableEntity.cabinet = "Ауд. 210";
        timeTableEntity.subGroup = "2";
        timeTableEntity.subjName = "Английский язык";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = getLessonTime(1, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(1, 0, 50);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 4;
        timeTables.add(timeTableEntity);

        // Вторые пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 6;
        timeTableEntity.cabinet = "online";
        timeTableEntity.subGroup = "1";
        timeTableEntity.subjName = "Программирование на Java";
        timeTableEntity.corp = "ONLINE";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = getLessonTime(1, 1, 10);
        timeTableEntity.timeEnd = getLessonTime(1, 2, 30);
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 7;
        timeTableEntity.cabinet = "Ауд. 210";
        timeTableEntity.subGroup = "2";
        timeTableEntity.subjName = "Математическая логика";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = getLessonTime(1, 1, 10);
        timeTableEntity.timeEnd = getLessonTime(1, 2, 30);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        // Послезавтра
        // Первые пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 8;
        timeTableEntity.cabinet = "Ауд. 301";
        timeTableEntity.subGroup = "5";
        timeTableEntity.subjName = "Математический анализ";
        timeTableEntity.corp = "Бульвар Гагарина 37";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = getLessonTime(2, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(2, 0, 50);
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 9;
        timeTableEntity.cabinet = "Ауд. 201";
        timeTableEntity.subGroup = "6";
        timeTableEntity.subjName = "Основы алгоритмизации";
        timeTableEntity.corp = "Бульвар Гагарина 37";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = getLessonTime(2, 0, -30);
        timeTableEntity.timeEnd = getLessonTime(2, 0, 50);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);

        // Вторые пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 10;
        timeTableEntity.cabinet = "Ауд. 110";
        timeTableEntity.subGroup = "5";
        timeTableEntity.subjName = "Теория вероятностей";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = getLessonTime(2, 1, 10);
        timeTableEntity.timeEnd = getLessonTime(2, 2, 30);
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 4;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 11;
        timeTableEntity.cabinet = "Ауд. 302";
        timeTableEntity.subGroup = "1";
        timeTableEntity.subjName = "Системное программирование";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = getLessonTime(2, 1, 10);
        timeTableEntity.timeEnd = getLessonTime(2, 2, 30);
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        DatabaseManager.getInstance(context).getHseDao().insertTimeTable(timeTables);
    }

    private Date getLessonTime(int days, int hours, int minutes) {
        // Получаем текущую дату и время
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Добавляем days дней, hours часов и minutes минут
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);

        // Возвращаем полученное время в виде объекта Date
        return calendar.getTime();
    }
}
