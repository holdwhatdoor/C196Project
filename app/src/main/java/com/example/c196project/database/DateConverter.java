package com.example.c196project.database;

import android.widget.EditText;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date calDtToDate(Date calDate){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(calDate);

        Date formattedDate = toDate(strDate);

        return formattedDate;
    }

    // Update Start/End Date EditText
    public static void updateDateText(EditText date, Calendar calendar){
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());

        date.setText(sdf.format(calendar.getTime()));
    }

    public static String formatDateString(String dateToFormat){
        String stringDateFormat = null;
        try {
            Date formattedDate = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy").parse(dateToFormat);

            Calendar cal = Calendar.getInstance();
            cal.setTime(formattedDate);
            stringDateFormat = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" +
                    cal.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            return stringDateFormat;
        }
    }
}
