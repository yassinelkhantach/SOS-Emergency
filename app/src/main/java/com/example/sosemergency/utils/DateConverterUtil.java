package com.example.sosemergency.utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * This class provides TypeConverter methods for converting Date objects to Long timestamps
 * and vice versa. It is used in conjunction with Room database to store and retrieve Date
 * objects in the database.
 *
 * TypeConverter Methods:
 *   - toDate: Converts a Long timestamp to a Date object.
 *   - toTimestamp: Converts a Date object to a Long timestamp.
 */
public class DateConverterUtil {

    /*
     * Converts a Long timestamp to a Date object.
     *
     * @param timestamp - The Long timestamp to be converted.
     * @return Date - The Date object converted from the timestamp.
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /*
     * Converts a Date object to a Long timestamp.
     *
     * @param date - The Date object to be converted.
     * @return Long - The Long timestamp converted from the Date object.
     */
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /*
     * Parses a date string into a Date object
     *
     * @param date - The Date object to be converted.
     * @return Long - The Long timestamp converted from the Date object.
     */
    public static Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
