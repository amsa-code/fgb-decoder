package com.amsa.fgb.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import com.amsa.fgb.Decoder;
import com.amsa.fgb.Formatter;

public class CreateComplianceKitMain {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        long[] count = new long[1];
        long[] errors = new long[1];
        File file = new File("/home/dxm/beacons.txt");
        File files = new File("target/files");
        files.mkdirs();
        Arrays.stream(files.listFiles()).forEach(File::delete);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            br.lines() //
                    .map(x -> x.trim()) //
                    .filter(x -> !x.isEmpty()) //
                    .forEach(x -> {
                        try {
                            String json = Decoder.decodeFull(x, Formatter.JSON);
                            Files.write(new File(files, x + ".json").toPath(), json.getBytes(StandardCharsets.UTF_8),
                                    StandardOpenOption.CREATE_NEW);
                        } catch (RuntimeException e) {
                            errors[0]++;
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        count[0]++;
                        if (count[0] % 1000 == 0) {
                            System.out.println("count=" + count[0] + ", errors=" + errors[0]);
                        }
                    });
        }
    }

}
