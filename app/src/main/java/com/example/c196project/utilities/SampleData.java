package com.example.c196project.utilities;

import com.example.c196project.database.TermEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {

    private static Calendar cal = new GregorianCalendar();

    private static SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

    private static String SAMPLE_TITLE_1 = "Term 1";
    private static Date SAMPLE_START_DATE_1 = Calendar.getInstance().getTime();
    private static String formattedDate1 = df.format(SAMPLE_START_DATE_1);
    private static Date SAMPLE_END_DATE_1 =  new Date(2019, 11, 23, 13, 47);


    private static String SAMPLE_TITLE_2 = "Term 2";
    private static Date SAMPLE_START_DATE_2 = Calendar.getInstance().getTime();
    private static String formattedDate2 = df.format(SAMPLE_START_DATE_2);
    private static Date SAMPLE_END_DATE_2 =  new Date(2020, 4, 10, 01, 22);


    public static List<TermEntity> getTerms(){
        List<TermEntity> terms = new ArrayList<>();
        terms.add(new TermEntity(0,SAMPLE_TITLE_1, SAMPLE_START_DATE_1, SAMPLE_END_DATE_1));
        terms.add(new TermEntity(0,SAMPLE_TITLE_2, SAMPLE_START_DATE_2, SAMPLE_END_DATE_2));

        System.out.println(terms.toString());

        return terms;
    }
}
