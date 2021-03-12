package com.amsa.fgb.internal;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amsa.fgb.Decoder;
import com.github.davidmoten.guavamini.annotations.VisibleForTesting;

public final class Debug {

    private static String lastHexStr;

    private static Map<String, List<String>> found = new HashMap<>();

    private static boolean searching = false;

    private Debug() {
        // prevent instantiation
    }

    public static void setLastHexStr(String hex) {
        lastHexStr = hex;
    }

    public static void startSearching() {
        searching = true;
    }

    public static void found() {
        if (searching) {
            StackTraceElement s = Thread.currentThread().getStackTrace()[2];
            String key = s.getClassName() + ":" + s.getMethodName() + ":" + s.getLineNumber();
            List<String> list = found.get(key);
            if (list == null) {
                list = new ArrayList<>();
                found.put(key, list);
            }
            list.add(lastHexStr);
            System.out.println("found " + lastHexStr + ", key=" + key);
        }
    }

    public static void writeFoundToComplianceKit() {
        searching = false;
        for (Entry<String, List<String>> entry : found.entrySet()) {
            System.out.println("writing found for " + entry.getKey());
            for (String hex : entry.getValue()) {
                if (hex != null) {
                    try {
                        String json = Decoder.decodeFullAsJson(hex);
                        File f = new File("src/test/resources/compliance-kit/" + hex + ".json");
                        writeToFile(json, f);
                        break;
                    } catch (RuntimeException e) {
                        System.out.println("errored " + hex);
                    }
                }
            }
        }
    }

    @VisibleForTesting
    static void writeToFile(String json, File f) {
        if (!f.exists()) {
            try {
                Files.write(f.toPath(), json.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            System.out.println("added " + f.getName() + " to compliance-kit");
        } else {
            System.out.println("already added " + f.getName());
        }
    }

    public static void stopSearching() {
        searching = false;
    }
}
