package com.uoc.inmo.gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class DateUtils {
    
    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public static String formatDate(Date d){
        if(d == null)
            return "";

        SimpleDateFormat format = new SimpleDateFormat(DD_MM_YYYY);

        return format.format(d);
    }
}
