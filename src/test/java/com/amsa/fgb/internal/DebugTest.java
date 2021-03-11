package com.amsa.fgb.internal;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UncheckedIOException;

import org.junit.Test;

public class DebugTest {

    @Test
    public void test() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        PrintStream previous = System.out;
        System.setOut(out);
        try {
            Debug.lastHexStr = "020C0200007FDFFF128F7700000000";
            Debug.startSearching();
            Debug.found();
            Debug.writeFoundToComplianceKit();
            Debug.stopSearching();
        } finally {
            System.setOut(previous);
        }
    }

    @Test
    public void testWriteToFile() {
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        PrintStream previous = System.out;
        System.setOut(out);
        try {
            File file = new File("target/temp.json");
            file.delete();
            Debug.writeToFile("[]", file);
            assertTrue(file.exists());
        } finally {
            System.setOut(previous);
        }
    }

    @Test(expected = UncheckedIOException.class)
    public void testWriteToFileFolderDoesNotExist() {
        File file = new File("target/non-existent-folder/temp.json");
        Debug.writeToFile("[]", file);
    }

}
