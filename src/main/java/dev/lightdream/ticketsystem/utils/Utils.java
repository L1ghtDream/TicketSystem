package dev.lightdream.ticketsystem.utils;

import dev.lightdream.ticketsystem.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String msToDate(Long ms) {
        Date date = new Date(ms);
        DateFormat format = new SimpleDateFormat(Main.instance.config.dateFormat);
        return format.format(date);
    }

}
