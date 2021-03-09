package com.amsa.fgb.internal;

import java.io.File;

public class Util {
    
    public static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        if (!file.delete()) {
            throw new RuntimeException("delete failed of " + file);
        }
    }
}
