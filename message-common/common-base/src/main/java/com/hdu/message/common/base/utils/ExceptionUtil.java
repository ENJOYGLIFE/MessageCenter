package com.hdu.message.common.base.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    /**
     * 将 Exception 转化为 String
     */
    public static String getExceptionToString(Throwable e) {
        if (e == null){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
