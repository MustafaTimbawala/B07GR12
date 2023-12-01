package com.example.b07project;

import androidx.annotation.NonNull;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Adapter for the LocalDateTime class
 * Provides a cleaner interface to work with
 * Integrates with FireBase
 */
public class DateTime implements Comparable<DateTime> {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public DateTime() {} //need this to write to database

    public DateTime(int year, int month, int day, int hour, int minute) throws DateTimeException {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        try {
            toLocalDateTime();
        } catch (DateTimeException exception) {
             throw exception;
        }
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    private String monthAsString() {
        Month m = Month.from(toLocalDateTime());
        return m.getDisplayName(TextStyle.FULL, Locale.CANADA);
    }

    public static DateTime now() {
        LocalDateTime dt = LocalDateTime.now();
        return new DateTime(dt.getYear(),
                            dt.getMonthValue(),
                            dt.getDayOfMonth(),
                            dt.getHour(),
                            dt.getMinute());
    }

    public int compareTo(DateTime other) {
        LocalDateTime myValue = this.toLocalDateTime();
        LocalDateTime otherValue = other.toLocalDateTime();
        return myValue.compareTo(otherValue);
    }

    @NonNull
    @Override
    public String toString() {
        String dateAsString = monthAsString() + " " + day + ", " + year;
        String hourAsString = "" + hour;
        String minuteAsString = "" + minute;
        if (hourAsString.length() == 1) {
            hourAsString = "0" + hourAsString;
        }
        if (minuteAsString.length() == 1) {
            minuteAsString = "0" + minuteAsString;
        }
        String timeAsString = hourAsString + ":" + minuteAsString;
        return dateAsString + " at " + timeAsString;
    }

}
