package com.tretiakov.absframework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrewtretiakov; 3/21/17.
 */

public class Utils {

    public static List emptyList() {
        return new ArrayList();
    }

    public static String firstCharToUp(String arg) {
        StringBuilder sb = new StringBuilder(arg);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
