package com.amsa.fgb.internal;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.amsa.fgb.Decoder;
import com.amsa.fgb.Formatter;

public final class Debug {

    public static String lastHexStr;

    private static List<String> found = new ArrayList<>();
    
    private static boolean searching = true;

    private Debug() {
        // prevent instantiation
    }

    public static void found() {
        if (searching) {
            found.add(lastHexStr);
            System.out.println("found " + lastHexStr);
        }
    }

    public static void writeFoundToComplianceKit() {
        searching = false;
        List<String> hexes = Debug.found;
        for (String hex : hexes) {
            if (hex != null) {
                try {
                    String json = Decoder.decodeFull(hex, Formatter.JSON);
                    File f = new File("src/test/resources/compliance-kit/" + hex + ".json");
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
                    return;
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
