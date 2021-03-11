package com.amsa.fgb.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.amsa.fgb.Decoder;

public final class DecodeHistoricalFilesMain {

    public static void decode() throws IOException {
        long[] count = new long[1];
        long[] errors = new long[1];
        String home = System.getProperty("user.home");
        File file = new File(home, "beacons.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            br.lines() //
                    .map(x -> x.trim()) //
                    .filter(x -> !x.isEmpty()) //
                    .forEach(x -> {
                        try {
                            Decoder.decodeFullAsJson(x);
                        } catch (RuntimeException e) {
                            errors[0]++;
                        }
                        count[0]++;
                        if (count[0] % 10000 == 0) {
                            System.out.println("count=" + count[0] + ", errors=" + errors[0]);
                        }
                    });
        }
    }
}
