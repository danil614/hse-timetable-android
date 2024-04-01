package org.hse.android.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date[] getWeekBoundary(Date currentDate) {
        // Создаем календарь и устанавливаем дату currentDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Получаем первый день недели
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        // Устанавливаем календарь на первый день недели и начало дня
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfWeek = calendar.getTime();

        // Добавляем 6 дней для получения конца недели
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        // Устанавливаем конец дня
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfWeek = calendar.getTime();

        // Возвращаем начало и конец недели в виде массива
        return new Date[]{startOfWeek, endOfWeek};
    }

    public static Date[] getDayBoundary(Date currentDate) {
        // Создаем календарь и устанавливаем дату currentDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Начало дня
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfDay = calendar.getTime();

        // Конец дня
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfDay = calendar.getTime();

        // Возвращаем начало и конец дня в виде массива
        return new Date[]{startOfDay, endOfDay};
    }
}
