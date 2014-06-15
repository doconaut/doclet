package com.doconaut.doclet;

/**
 * @author Hannes Dorfmann
 */
public class StringUtils {

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }
}
