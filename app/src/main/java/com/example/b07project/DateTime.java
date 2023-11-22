package com.example.b07project;

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

    public DateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
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

    public String getMonthAsString() {
        Month m = Month.from(toLocalDateTime());
        return m.getDisplayName(TextStyle.FULL, Locale.CANADA);
    }

    public String getDayAsString() {
        DayOfWeek d = DayOfWeek.from(toLocalDateTime());
        return d.getDisplayName(TextStyle.FULL, Locale.CANADA);
    }

    public int compareTo(DateTime other) {
        LocalDateTime myValue = this.toLocalDateTime();
        LocalDateTime otherValue = other.toLocalDateTime();
        return myValue.compareTo(otherValue);
    }

}
