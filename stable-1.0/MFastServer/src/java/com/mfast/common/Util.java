/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.common;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.Timestamp;

/**
 *
 * @author Saqib
 */
public class Util {
    public static Date getDateFromString(String input) throws Exception{
        Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(input);
        return date;
    }
    
    public static Timestamp getTS(){
        java.util.Date date = new java.util.Date();
        return new Timestamp(date.getTime());
    }
}
