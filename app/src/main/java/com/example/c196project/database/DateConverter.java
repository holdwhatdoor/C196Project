package com.example.c196project.database;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp){

        return timestamp == null ? null : new Date(timestamp);
    }


    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }


    public static Date toDate(String dateString){

        Date date = null;
        try {
            date = new SimpleDateFormat("mm/dd/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date calDtToDate(Date calDate){
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        String strDate = dateFormat.format(calDate);

        Date formattedDate = toDate(strDate);

        return formattedDate;
    }
}
