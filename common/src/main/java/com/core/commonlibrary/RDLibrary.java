package com.core.commonlibrary;

public class RDLibrary {

    private RDLibrary() {
    }

    private static boolean isDebug;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static boolean isDebug() {
        return isDebug;
    }
}
