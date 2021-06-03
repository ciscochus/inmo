package com.uoc.inmo.gui.util;

import java.util.Locale;

import lombok.Data;

@Data
public class NumberUtils {
    
    public static String getFormatPrice(Double price){
        if(price == null)
            return "0";
            
        return String.format(
                Locale.GERMAN, "%,.2f", price);
    }

}
