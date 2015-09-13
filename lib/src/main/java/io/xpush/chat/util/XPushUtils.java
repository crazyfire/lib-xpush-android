package io.xpush.chat.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;
import java.util.UUID;

import io.xpush.chat.ApplicationController;

public class XPushUtils {

    public static String generateChannelId( ArrayList<String> users ){
        if( users.size() > 2 ){
            return getUniqueKey()+"^"+ ApplicationController.getInstance().getAppId();
        } else {
            // 1:1 channel = userId concat friendId
            ArrayList<String> temp = users;
            Collections.sort(temp, new NameAscCompare());

            return TextUtils.join("$", temp) +"^"+ApplicationController.getInstance().getAppId();
        }
    }

    public static String getUniqueKey( ){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    static class NameAscCompare implements Comparator<String> {
        public int compare(String arg0, String arg1) {
            return arg0.compareTo( arg1 );
        }
    }
}