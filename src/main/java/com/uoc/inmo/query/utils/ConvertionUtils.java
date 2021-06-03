package com.uoc.inmo.query.utils;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

public class ConvertionUtils {
    
    public static String toString(Blob source){
        if(source == null)
            return null;

        try {
            return new String(source.getBytes(1l, (int) source.length()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Blob toBlob(String source){
        if(source == null)
            return null;

        try {
            return new SerialBlob(source.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return null;
    }

    public static String dateToString(Date source){
        if(source == null)
            return null;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(source);
    }
}
