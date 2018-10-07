package com.raye7.news.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UIManager {
    public static String getDateFormat(String publishedDate) {
        String convertedPublishedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = dateFormat.parse(publishedDate);
            Format formatter = new SimpleDateFormat("dd MMM yyyy");
            convertedPublishedDate = formatter.format(date);
            return convertedPublishedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedPublishedDate;
    }
}
